package com.example.forum.repository;

import com.example.forum.model.Post;
import com.example.forum.model.PostLike;
import com.example.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<PostLike> findByUserAndPost(User user, Post post);

    long countByPost(Post post);
}