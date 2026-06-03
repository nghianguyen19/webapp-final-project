package com.example.forum.service;

import com.example.forum.model.Bookmark;
import com.example.forum.model.Post;
import com.example.forum.model.User;
import com.example.forum.repository.BookmarkRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public BookmarkService(
            BookmarkRepository bookmarkRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void toggleBookmark(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        bookmarkRepository.findByUserAndPost(user, post)
                .ifPresentOrElse(
                        bookmarkRepository::delete,
                        () -> bookmarkRepository.save(new Bookmark(user, post))
                );
    }

    public boolean isBookmarkedByUser(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return bookmarkRepository.existsByUserAndPost(user, post);
    }

    public List<Bookmark> getBookmarksByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookmarkRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public long countBookmarks(Post post) {
        return bookmarkRepository.countByPost(post);
    }
}