package com.example.forum.controller;

import com.example.forum.model.Post;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.service.BookmarkService;
import com.example.forum.service.CommentService;
import com.example.forum.service.LikeService;
import com.example.forum.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final CategoryRepository categoryRepository;
    private final LikeService likeService;
    private final BookmarkService bookmarkService;

    public PostController(
            PostService postService,
            CommentService commentService,
            CategoryRepository categoryRepository,
            LikeService likeService,
            BookmarkService bookmarkService
    ) {
        this.postService = postService;
        this.commentService = commentService;
        this.categoryRepository = categoryRepository;
        this.likeService = likeService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/posts/{id}")
    public String postDetail(
            @PathVariable Long id,
            Model model,
            Principal principal
    ) {
        Post post = postService.getPostByIdAndIncreaseView(id);

        boolean canManagePost = false;
        boolean likedByCurrentUser = false;
        boolean bookmarkedByCurrentUser = false;

        if (principal != null) {
            canManagePost = postService.canManagePost(post, principal.getName());
            likedByCurrentUser = likeService.isLikedByUser(id, principal.getName());
            bookmarkedByCurrentUser = bookmarkService.isBookmarkedByUser(id, principal.getName());
        }

        model.addAttribute("pageTitle", post.getTitle());
        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getCommentsByPost(post));
        model.addAttribute("canManagePost", canManagePost);
        model.addAttribute("likedByCurrentUser", likedByCurrentUser);
        model.addAttribute("bookmarkedByCurrentUser", bookmarkedByCurrentUser);
        model.addAttribute("likeCount", likeService.countLikes(post));
        model.addAttribute("bookmarkCount", bookmarkService.countBookmarks(post));

        return "post-detail";
    }

    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        model.addAttribute("pageTitle", "Create New Post");
        model.addAttribute("categories", categoryRepository.findAll());

        return "create-post";
    }

    @PostMapping("/posts/new")
    public String createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Long categoryId,
            Principal principal
    ) {
        Post post = postService.createPost(title, content, categoryId, principal.getName());

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/edit/{id}")
    public String editPostForm(
            @PathVariable Long id,
            Model model,
            Principal principal
    ) {
        Post post = postService.getPostById(id);

        if (!postService.canManagePost(post, principal.getName())) {
            return "redirect:/?unauthorized";
        }

        model.addAttribute("pageTitle", "Edit Post");
        model.addAttribute("post", post);
        model.addAttribute("categories", categoryRepository.findAll());

        return "edit-post";
    }

    @PostMapping("/posts/edit/{id}")
    public String updatePost(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Long categoryId,
            Principal principal
    ) {
        postService.updatePost(id, title, content, categoryId, principal.getName());

        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/delete/{id}")
    public String deletePost(
            @PathVariable Long id,
            Principal principal
    ) {
        postService.deletePost(id, principal.getName());

        return "redirect:/";
    }
}