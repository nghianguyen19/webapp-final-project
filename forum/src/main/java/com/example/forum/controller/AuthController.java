package com.example.forum.controller;

import com.example.forum.dto.RegisterDto;
import com.example.forum.model.User;
import com.example.forum.repository.UserRepository;
import com.example.forum.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Random;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("pageTitle", "Login");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("pageTitle", "Register");
        model.addAttribute("registerDto", new RegisterDto());

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("registerDto") RegisterDto registerDto,
            Model model
    ) {
        String username = registerDto.getUsername() == null ? "" : registerDto.getUsername().trim();
        String fullName = registerDto.getFullName() == null ? "" : registerDto.getFullName().trim();
        String email = registerDto.getEmail() == null ? "" : registerDto.getEmail().trim();
        String password = registerDto.getPassword() == null ? "" : registerDto.getPassword();
        String confirmPassword = registerDto.getConfirmPassword() == null ? "" : registerDto.getConfirmPassword();

        if (username.isBlank() || fullName.isBlank() || email.isBlank() || password.isBlank()) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("error", "Please fill in all required fields.");
            return "register";
        }

        if (username.length() < 3) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("error", "Username must be at least 3 characters.");
            return "register";
        }

        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("error", "Username can only contain letters, numbers, and underscores.");
            return "register";
        }

        if (userRepository.existsByUsername(username)) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("error", "Username already exists.");
            return "register";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("error", "Email already exists.");
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        if (password.length() < 6) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "register";
        }

        String otpCode = generateOtpCode();

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        user.setStatus("PENDING");
        user.setBio("New member of IU Forum.");
        user.setAvatarUrl("/images/default-avatar.png");
        user.setOtpCode(otpCode);
        user.setOtpExpiredAt(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        try {
            emailService.sendOtpEmail(email, otpCode);
        } catch (Exception e) {
            userRepository.delete(user);

            model.addAttribute("pageTitle", "Register");
            model.addAttribute("registerDto", registerDto);
            model.addAttribute("error", "OTP email could not be sent. Please check mail configuration.");
            return "register";
        }

        return "redirect:/verify-otp?email=" + email;
    }

    @GetMapping("/verify-otp")
    public String verifyOtpPage(
            @RequestParam String email,
            Model model
    ) {
        model.addAttribute("pageTitle", "Verify OTP");
        model.addAttribute("email", email);

        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otpCode,
            Model model
    ) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            model.addAttribute("pageTitle", "Verify OTP");
            model.addAttribute("email", email);
            model.addAttribute("error", "Account not found.");
            return "verify-otp";
        }

        if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
            return "redirect:/login?verified";
        }

        if (user.getOtpCode() == null || user.getOtpExpiredAt() == null) {
            model.addAttribute("pageTitle", "Verify OTP");
            model.addAttribute("email", email);
            model.addAttribute("error", "OTP is missing. Please resend OTP.");
            return "verify-otp";
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiredAt())) {
            model.addAttribute("pageTitle", "Verify OTP");
            model.addAttribute("email", email);
            model.addAttribute("error", "OTP has expired. Please resend OTP.");
            return "verify-otp";
        }

        if (!user.getOtpCode().equals(otpCode.trim())) {
            model.addAttribute("pageTitle", "Verify OTP");
            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid OTP code.");
            return "verify-otp";
        }

        user.setStatus("ACTIVE");
        user.setOtpCode(null);
        user.setOtpExpiredAt(null);

        userRepository.save(user);

        return "redirect:/login?verified";
    }

    @PostMapping("/resend-otp")
    public String resendOtp(
            @RequestParam String email,
            Model model
    ) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            model.addAttribute("pageTitle", "Verify OTP");
            model.addAttribute("email", email);
            model.addAttribute("error", "Account not found.");
            return "verify-otp";
        }

        if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
            return "redirect:/login?verified";
        }

        String otpCode = generateOtpCode();

        user.setOtpCode(otpCode);
        user.setOtpExpiredAt(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        try {
            emailService.sendOtpEmail(email, otpCode);
        } catch (Exception e) {
            model.addAttribute("pageTitle", "Verify OTP");
            model.addAttribute("email", email);
            model.addAttribute("error", "Could not resend OTP. Please check mail configuration.");
            return "verify-otp";
        }

        model.addAttribute("pageTitle", "Verify OTP");
        model.addAttribute("email", email);
        model.addAttribute("success", "A new OTP has been sent to your email.");

        return "verify-otp";
    }

    private String generateOtpCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}