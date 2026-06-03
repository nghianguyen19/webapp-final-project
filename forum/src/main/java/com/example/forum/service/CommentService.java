package com.example.forum.service;

import com.example.forum.model.Comment;
import com.example.forum.model.Post;
import com.example.forum.model.User;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findByPostOrderByCreatedAtAsc(post);
    }

    public Comment addComment(Long postId, String content, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (Boolean.TRUE.equals(post.getLocked())) {
            throw new RuntimeException("This post is locked");
        }

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment(content, author, post);
        Comment savedComment = commentRepository.save(comment);

        if (!post.getAuthor().getUsername().equals(username)) {
            String message = username + " commented on your post: " + post.getTitle();
            notificationService.createNotification(post.getAuthor(), post, message);
        }

        return savedComment;
    }

    public Long deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());
        boolean isOwner = comment.getAuthor().getUsername().equals(username);

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("You do not have permission to delete this comment");
        }

        Long postId = comment.getPost().getId();

        commentRepository.delete(comment);

        return postId;
    }
}