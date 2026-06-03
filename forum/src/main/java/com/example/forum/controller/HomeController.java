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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostService postService;

    public HomeController(
            PostRepository postRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            CommentRepository commentRepository,
            PostService postService
    ) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        int pageSize = 5;
        Page<Post> postPage = postService.getPostsPage(page, pageSize);

        model.addAttribute("pageTitle", "Campus Forum");
        model.addAttribute("postPage", postPage);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalComments", commentRepository.count());
        model.addAttribute("pinnedPosts", postRepository.findTop5ByPinnedTrueOrderByCreatedAtDesc());

        return "index";
    }
}