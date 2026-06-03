package com.example.forum.controller;

import com.example.forum.service.LikeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/posts/like/{id}")
    public String toggleLike(
            @PathVariable Long id,
            Principal principal
    ) {
        likeService.toggleLike(id, principal.getName());

        return "redirect:/posts/" + id;
    }
}