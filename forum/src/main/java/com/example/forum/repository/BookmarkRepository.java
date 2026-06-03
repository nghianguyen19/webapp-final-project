package com.example.forum.repository;

import com.example.forum.model.Bookmark;
import com.example.forum.model.Post;
import com.example.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<Bookmark> findByUserAndPost(User user, Post post);

    List<Bookmark> findByUserOrderByCreatedAtDesc(User user);

    long countByPost(Post post);
}