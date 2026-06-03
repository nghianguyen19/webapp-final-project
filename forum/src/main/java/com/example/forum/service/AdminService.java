package com.example.forum.service;

import com.example.forum.model.Category;
import com.example.forum.model.Post;
import com.example.forum.model.User;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public AdminService(
            UserRepository userRepository,
            PostRepository postRepository,
            CategoryRepository categoryRepository
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
            user.setStatus("LOCKED");
        } else {
            user.setStatus("ACTIVE");
        }

        userRepository.save(user);
    }

    public void makeAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole("ROLE_ADMIN");
        userRepository.save(user);
    }

    public void makeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postRepository.delete(post);
    }

    public void togglePostPin(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setPinned(!Boolean.TRUE.equals(post.getPinned()));
        postRepository.save(post);
    }

    public void togglePostLock(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setLocked(!Boolean.TRUE.equals(post.getLocked()));
        postRepository.save(post);
    }

    public Category createCategory(String name, String description, String color) {
        String slug = generateSlug(name);

        if (categoryRepository.existsByName(name)) {
            throw new RuntimeException("Category name already exists");
        }

        if (categoryRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Category category = new Category();
        category.setName(name);
        category.setSlug(slug);
        category.setDescription(description);
        category.setColor(color);

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }

    private String generateSlug(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        String slug = normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace("đ", "d")
                .replace("Đ", "d")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if (slug.isBlank()) {
            slug = "category";
        }

        return slug;
    }
}