package com.example.forum.repository;

import com.example.forum.model.RoleDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleDefinitionRepository extends JpaRepository<RoleDefinition, Long> {

    Optional<RoleDefinition> findByRoleCode(String roleCode);

    boolean existsByRoleCode(String roleCode);

    List<RoleDefinition> findAllByOrderByRoleCodeAsc();
}