package com.example.forum.controller;

import com.example.forum.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService
    ) {
        this.reportService = reportService;
    }

    @PostMapping("/reports/post/{postId}")
    public String reportPost(
            @PathVariable Long postId,
            @RequestParam String reason,
            @RequestParam(required = false) String description,
            Principal principal
    ) {
        reportService.reportPost(postId, principal.getName(), reason, description);

        return "redirect:/posts/" + postId + "?reported";
    }

    @PostMapping("/reports/comment/{commentId}")
    public String reportComment(
            @PathVariable Long commentId,
            @RequestParam Long postId,
            @RequestParam String reason,
            @RequestParam(required = false) String description,
            Principal principal
    ) {
        reportService.reportComment(commentId, principal.getName(), reason, description);

        return "redirect:/posts/" + postId + "?reported";
    }
}