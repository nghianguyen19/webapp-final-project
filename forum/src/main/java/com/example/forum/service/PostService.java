package com.example.forum.service;

import com.example.forum.model.Category;
import com.example.forum.model.Post;
import com.example.forum.model.User;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public PostService(
            PostRepository postRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Page<Post> getPostsPage(int page, int size) {
        int safePage = Math.max(page, 0);
        return postRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(safePage, size));
    }

    public Page<Post> searchPosts(String keyword, int page, int size) {
        int safePage = Math.max(page, 0);
        String safeKeyword = keyword == null ? "" : keyword.trim();

        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc(
                safeKeyword,
                safeKeyword,
                PageRequest.of(safePage, size)
        );
    }

    public Page<Post> getPostsByCategorySlug(String slug, int page, int size) {
        int safePage = Math.max(page, 0);
        return postRepository.findByCategorySlugOrderByCreatedAtDesc(
                slug,
                PageRequest.of(safePage, size)
        );
    }

    public List<Post> getPostsByCategorySlug(String slug) {
        return postRepository.findByCategorySlugOrderByCreatedAtDesc(slug);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post getPostByIdAndIncreaseView(Long id) {
        Post post = getPostById(id);

        if (post.getViewCount() == null) {
            post.setViewCount(0);
        }

        post.setViewCount(post.getViewCount() + 1);
        return postRepository.save(post);
    }

    public Post createPost(String title, String content, Long categoryId, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String slug = generateSlug(title);

        Post post = new Post(title, slug, content, author, category);

        return postRepository.save(post);
    }

    public Post updatePost(Long id, String title, String content, Long categoryId, String username) {
        Post post = getPostById(id);

        if (!canManagePost(post, username)) {
            throw new RuntimeException("You do not have permission to edit this post");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        post.setTitle(title);
        post.setSlug(generateSlug(title));
        post.setContent(content);
        post.setCategory(category);
        post.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    public void deletePost(Long id, String username) {
        Post post = getPostById(id);

        if (!canManagePost(post, username)) {
            throw new RuntimeException("You do not have permission to delete this post");
        }

        postRepository.delete(post);
    }

    public boolean canManagePost(Post post, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());
        boolean isOwner = post.getAuthor().getUsername().equals(username);

        return isAdmin || isOwner;
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
            slug = "post";
        }

        if (slug.length() > 180) {
            slug = slug.substring(0, 180);
        }

        return slug + "-" + System.currentTimeMillis();
    }
}