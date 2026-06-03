package com.example.forum.controller;

import com.example.forum.service.BookmarkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/posts/bookmark/{id}")
    public String toggleBookmark(
            @PathVariable Long id,
            Principal principal
    ) {
        bookmarkService.toggleBookmark(id, principal.getName());

        return "redirect:/posts/" + id;
    }

    @GetMapping("/bookmarks")
    public String bookmarks(
            Model model,
            Principal principal
    ) {
        model.addAttribute("pageTitle", "My Bookmarks");
        model.addAttribute("bookmarks", bookmarkService.getBookmarksByUsername(principal.getName()));

        return "bookmarks";
    }
}