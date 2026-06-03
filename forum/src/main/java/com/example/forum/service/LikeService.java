package com.example.forum.service;

import com.example.forum.model.Post;
import com.example.forum.model.PostLike;
import com.example.forum.model.User;
import com.example.forum.repository.PostLikeRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(
            PostLikeRepository postLikeRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void toggleLike(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postLikeRepository.findByUserAndPost(user, post)
                .ifPresentOrElse(
                        postLikeRepository::delete,
                        () -> postLikeRepository.save(new PostLike(user, post))
                );
    }

    public boolean isLikedByUser(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return postLikeRepository.existsByUserAndPost(user, post);
    }

    public long countLikes(Post post) {
        return postLikeRepository.countByPost(post);
    }
}