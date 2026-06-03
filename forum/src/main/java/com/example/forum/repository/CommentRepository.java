package com.example.forum.repository;

import com.example.forum.model.Comment;
import com.example.forum.model.Post;
import com.example.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostOrderByCreatedAtAsc(Post post);

    long countByPost(Post post);

    long countByAuthor(User author);
}