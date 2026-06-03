package com.example.forum.controller;

import com.example.forum.model.User;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.UserRepository;
import com.example.forum.service.RoleDefinitionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Locale;
import java.util.UUID;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RoleDefinitionService roleDefinitionService;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(
            UserRepository userRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            RoleDefinitionService roleDefinitionService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.roleDefinitionService = roleDefinitionService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile/me")
    public String myProfile(Principal principal) {
        return "redirect:/profile/" + principal.getName();
    }

    @GetMapping("/profile/{username}")
    public String profile(
            @PathVariable String username,
            Model model
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("pageTitle", user.getUsername() + " Profile");
        model.addAttribute("profileUser", user);
        model.addAttribute("displayRole", roleDefinitionService.getDisplayNameByCode(user.getRole()));
        model.addAttribute("posts", postRepository.findByAuthorUsernameOrderByCreatedAtDesc(username));
        model.addAttribute("postCount", user.getPosts().size());
        model.addAttribute("commentCount", commentRepository.countByAuthor(user));

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(
            Model model,
            Principal principal
    ) {
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("pageTitle", "Edit Profile");
        model.addAttribute("user", currentUser);

        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) MultipartFile avatarFile,
            Model model,
            Principal principal,
            HttpServletRequest request
    ) {
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String cleanUsername = username == null ? "" : username.trim();
        String cleanFullName = fullName == null ? "" : fullName.trim();
        String cleanEmail = email == null ? "" : email.trim();
        String cleanBio = bio == null ? "" : bio.trim();

        if (cleanUsername.isBlank() || cleanFullName.isBlank() || cleanEmail.isBlank()) {
            model.addAttribute("pageTitle", "Edit Profile");
            model.addAttribute("user", currentUser);
            model.addAttribute("error", "Username, full name and email are required.");
            return "edit-profile";
        }

        if (cleanUsername.length() < 3) {
            model.addAttribute("pageTitle", "Edit Profile");
            model.addAttribute("user", currentUser);
            model.addAttribute("error", "Username must be at least 3 characters.");
            return "edit-profile";
        }

        if (!cleanUsername.matches("^[a-zA-Z0-9_]+$")) {
            model.addAttribute("pageTitle", "Edit Profile");
            model.addAttribute("user", currentUser);
            model.addAttribute("error", "Username can only contain letters, numbers, and underscores.");
            return "edit-profile";
        }

        if (userRepository.existsByUsername(cleanUsername)
                && !cleanUsername.equalsIgnoreCase(currentUser.getUsername())) {
            model.addAttribute("pageTitle", "Edit Profile");
            model.addAttribute("user", currentUser);
            model.addAttribute("error", "Username is already used by another account.");
            return "edit-profile";
        }

        if (userRepository.existsByEmail(cleanEmail)
                && !cleanEmail.equalsIgnoreCase(currentUser.getEmail())) {
            model.addAttribute("pageTitle", "Edit Profile");
            model.addAttribute("user", currentUser);
            model.addAttribute("error", "Email is already used by another account.");
            return "edit-profile";
        }

        boolean usernameChanged = !cleanUsername.equals(currentUser.getUsername());

        currentUser.setUsername(cleanUsername);
        currentUser.setFullName(cleanFullName);
        currentUser.setEmail(cleanEmail);
        currentUser.setBio(cleanBio);

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String originalFilename = avatarFile.getOriginalFilename();

                if (originalFilename == null || originalFilename.isBlank()) {
                    throw new RuntimeException("Invalid avatar file.");
                }

                String lowerName = originalFilename.toLowerCase(Locale.ROOT);

                boolean validImage =
                        lowerName.endsWith(".jpg")
                                || lowerName.endsWith(".jpeg")
                                || lowerName.endsWith(".png")
                                || lowerName.endsWith(".gif")
                                || lowerName.endsWith(".webp");

                if (!validImage) {
                    model.addAttribute("pageTitle", "Edit Profile");
                    model.addAttribute("user", currentUser);
                    model.addAttribute("error", "Avatar must be an image file: jpg, jpeg, png, gif, or webp.");
                    return "edit-profile";
                }

                if (avatarFile.getSize() > 5 * 1024 * 1024) {
                    model.addAttribute("pageTitle", "Edit Profile");
                    model.addAttribute("user", currentUser);
                    model.addAttribute("error", "Avatar file must be smaller than 5MB.");
                    return "edit-profile";
                }

                String extension = lowerName.substring(lowerName.lastIndexOf("."));

                String safeFileName = cleanUsername
                        + "-"
                        + UUID.randomUUID()
                        + extension;

                Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "avatars")
                        .toAbsolutePath()
                        .normalize();

                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(safeFileName)
                        .toAbsolutePath()
                        .normalize();

                try (InputStream inputStream = avatarFile.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                currentUser.setAvatarUrl("/uploads/avatars/" + safeFileName);

            } catch (Exception e) {
                model.addAttribute("pageTitle", "Edit Profile");
                model.addAttribute("user", currentUser);
                model.addAttribute("error", "Could not upload avatar: " + e.getMessage());
                return "edit-profile";
            }
        }

        userRepository.save(currentUser);

        if (usernameChanged) {
            SecurityContextHolder.clearContext();

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            return "redirect:/login?usernameChanged";
        }

        return "redirect:/profile/me?updated";
    }

    @GetMapping("/profile/password")
    public String changePasswordForm(
            Model model
    ) {
        model.addAttribute("pageTitle", "Change Password");

        return "change-password";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            Principal principal,
            HttpServletRequest request
    ) {
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String oldPass = currentPassword == null ? "" : currentPassword;
        String newPass = newPassword == null ? "" : newPassword;
        String confirmPass = confirmPassword == null ? "" : confirmPassword;

        if (oldPass.isBlank() || newPass.isBlank() || confirmPass.isBlank()) {
            model.addAttribute("pageTitle", "Change Password");
            model.addAttribute("error", "Please fill in all password fields.");
            return "change-password";
        }

        if (!passwordEncoder.matches(oldPass, currentUser.getPassword())) {
            model.addAttribute("pageTitle", "Change Password");
            model.addAttribute("error", "Current password is incorrect.");
            return "change-password";
        }

        if (newPass.length() < 6) {
            model.addAttribute("pageTitle", "Change Password");
            model.addAttribute("error", "New password must be at least 6 characters.");
            return "change-password";
        }

        if (!newPass.equals(confirmPass)) {
            model.addAttribute("pageTitle", "Change Password");
            model.addAttribute("error", "New password and confirm password do not match.");
            return "change-password";
        }

        if (passwordEncoder.matches(newPass, currentUser.getPassword())) {
            model.addAttribute("pageTitle", "Change Password");
            model.addAttribute("error", "New password must be different from current password.");
            return "change-password";
        }

        currentUser.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(currentUser);

        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/login?passwordChanged";
    }
}