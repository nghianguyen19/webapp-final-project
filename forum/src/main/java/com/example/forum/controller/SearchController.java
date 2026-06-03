package com.example.forum.controller;

import com.example.forum.model.Post;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.UserRepository;
import com.example.forum.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SearchController {

    private final PostService postService;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public SearchController(
            PostService postService,
            CategoryRepository categoryRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            CommentRepository commentRepository
    ) {
        this.postService = postService;
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "q", required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        int pageSize = 5;
        Page<Post> postPage = postService.searchPosts(keyword, page, pageSize);

        model.addAttribute("pageTitle", "Search");
        model.addAttribute("keyword", keyword);
        model.addAttribute("postPage", postPage);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalComments", commentRepository.count());

        return "search";
    }
}