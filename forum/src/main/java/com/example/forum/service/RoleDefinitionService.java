package com.example.forum.service;

import com.example.forum.model.RoleDefinition;
import com.example.forum.repository.RoleDefinitionRepository;
import com.example.forum.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleDefinitionService {

    private final RoleDefinitionRepository roleDefinitionRepository;
    private final UserRepository userRepository;

    public RoleDefinitionService(
            RoleDefinitionRepository roleDefinitionRepository,
            UserRepository userRepository
    ) {
        this.roleDefinitionRepository = roleDefinitionRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initDefaultRoles() {
        createDefaultRoleIfMissing("ROLE_ADMIN", "ADMIN", "Administrator role. Can access admin panel.");
        createDefaultRoleIfMissing("ROLE_USER", "USER", "Default member role.");
    }

    private void createDefaultRoleIfMissing(String roleCode, String displayName, String description) {
        if (!roleDefinitionRepository.existsByRoleCode(roleCode)) {
            roleDefinitionRepository.save(new RoleDefinition(roleCode, displayName, description));
        }
    }

    public List<RoleDefinition> getAllRoles() {
        return roleDefinitionRepository.findAllByOrderByRoleCodeAsc();
    }

    public RoleDefinition getRoleByCode(String roleCode) {
        return roleDefinitionRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public String getDisplayNameByCode(String roleCode) {
        return roleDefinitionRepository.findByRoleCode(roleCode)
                .map(RoleDefinition::getDisplayName)
                .orElse(formatRoleCode(roleCode));
    }

    public Map<String, String> getRoleLabelMap() {
        List<RoleDefinition> roles = getAllRoles();
        Map<String, String> map = new HashMap<>();

        for (RoleDefinition role : roles) {
            map.put(role.getRoleCode(), role.getDisplayName());
        }

        return map;
    }

    public void createRole(String roleCode, String displayName, String description) {
        String cleanRoleCode = normalizeRoleCode(roleCode);
        String cleanDisplayName = displayName == null ? "" : displayName.trim();
        String cleanDescription = description == null ? "" : description.trim();

        if (cleanRoleCode.isBlank() || cleanDisplayName.isBlank()) {
            throw new RuntimeException("Role code and display name are required.");
        }

        if (!cleanRoleCode.matches("^ROLE_[A-Z0-9_]+$")) {
            throw new RuntimeException("Role code must contain only letters, numbers, and underscores.");
        }

        if (roleDefinitionRepository.existsByRoleCode(cleanRoleCode)) {
            throw new RuntimeException("Role already exists.");
        }

        roleDefinitionRepository.save(new RoleDefinition(cleanRoleCode, cleanDisplayName, cleanDescription));
    }

    public void updateRole(Long id, String displayName, String description) {
        RoleDefinition role = roleDefinitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        String cleanDisplayName = displayName == null ? "" : displayName.trim();
        String cleanDescription = description == null ? "" : description.trim();

        if (cleanDisplayName.isBlank()) {
            throw new RuntimeException("Display name is required.");
        }

        role.setDisplayName(cleanDisplayName);
        role.setDescription(cleanDescription);

        roleDefinitionRepository.save(role);
    }

    public void deleteRole(Long id) {
        RoleDefinition role = roleDefinitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if ("ROLE_ADMIN".equals(role.getRoleCode()) || "ROLE_USER".equals(role.getRoleCode())) {
            throw new RuntimeException("Default roles cannot be deleted.");
        }

        long userCount = userRepository.countByRole(role.getRoleCode());

        if (userCount > 0) {
            throw new RuntimeException("Cannot delete this role because some users are using it.");
        }

        roleDefinitionRepository.delete(role);
    }

    private String normalizeRoleCode(String roleCode) {
        String clean = roleCode == null ? "" : roleCode.trim().toUpperCase();

        if (clean.isBlank()) {
            return "";
        }

        if (!clean.startsWith("ROLE_")) {
            clean = "ROLE_" + clean;
        }

        return clean;
    }

    private String formatRoleCode(String roleCode) {
        if (roleCode == null) {
            return "";
        }

        if (roleCode.startsWith("ROLE_")) {
            return roleCode.replace("ROLE_", "");
        }

        return roleCode;
    }
}