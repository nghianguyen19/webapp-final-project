package com.example.forum.controller;

import com.example.forum.model.Category;
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
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostService postService;

    public CategoryController(
            CategoryRepository categoryRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            CommentRepository commentRepository,
            PostService postService
    ) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    @GetMapping("/categories/{slug}")
    public String categoryPosts(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        int pageSize = 5;
        Page<Post> postPage = postService.getPostsByCategorySlug(slug, page, pageSize);

        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("category", category);
        model.addAttribute("postPage", postPage);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalComments", commentRepository.count());

        return "category";
    }
}