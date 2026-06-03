package com.example.forum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("IU Forum - Verify Your Account");
        message.setText(
                "Welcome to IU Forum!\n\n" +
                        "Your OTP verification code is: " + otpCode + "\n\n" +
                        "This code will expire in 10 minutes.\n\n" +
                        "If you did not register for IU Forum, please ignore this email."
        );

        mailSender.send(message);
    }
}