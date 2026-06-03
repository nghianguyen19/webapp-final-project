package com.example.forum.service;

import com.example.forum.dto.RegisterDto;
import com.example.forum.model.User;
import com.example.forum.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(RegisterDto registerDto) {
        String username = registerDto.getUsername().trim();
        String email = registerDto.getEmail().trim();
        String fullName = registerDto.getFullName() == null
                ? username
                : registerDto.getFullName().trim();

        if (username.isBlank()) {
            throw new RuntimeException("Username is required");
        }

        if (email.isBlank()) {
            throw new RuntimeException("Email is required");
        }

        if (registerDto.getPassword() == null || registerDto.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new RuntimeException("Password confirmation does not match");
        }

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole("ROLE_USER");
        user.setStatus("ACTIVE");
        user.setAvatarUrl("/images/default-avatar.png");
        user.setBio("New member of Campus Forum.");

        return userRepository.save(user);
    }
}