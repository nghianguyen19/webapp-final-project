package com.example.forum.controller;

import com.example.forum.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(
            CommentService commentService
    ) {
        this.commentService = commentService;
    }

    @PostMapping("/comments/add/{postId}")
    public String addComment(
            @PathVariable Long postId,
            @RequestParam String content,
            Principal principal
    ) {
        commentService.addComment(postId, content, principal.getName());

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/comments/delete/{id}")
    public String deleteComment(
            @PathVariable Long id,
            Principal principal
    ) {
        Long postId = commentService.deleteComment(id, principal.getName());

        return "redirect:/posts/" + postId;
    }
}