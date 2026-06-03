package com.example.forum.controller;

import com.example.forum.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public String notifications(
            Model model,
            Principal principal
    ) {
        model.addAttribute("pageTitle", "Notifications");
        model.addAttribute("notifications", notificationService.getNotificationsByUsername(principal.getName()));
        model.addAttribute("unreadCount", notificationService.countUnreadByUsername(principal.getName()));

        return "notifications";
    }

    @PostMapping("/notifications/read/{id}")
    public String markRead(
            @PathVariable Long id,
            Principal principal
    ) {
        notificationService.markAsRead(id, principal.getName());

        return "redirect:/notifications";
    }

    @PostMapping("/notifications/read-all")
    public String markAllRead(
            Principal principal
    ) {
        notificationService.markAllAsRead(principal.getName());

        return "redirect:/notifications";
    }
}