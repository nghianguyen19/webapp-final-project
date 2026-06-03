package com.example.forum.repository;

import com.example.forum.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findByCategorySlugOrderByCreatedAtDesc(String slug);

    Page<Post> findByCategorySlugOrderByCreatedAtDesc(String slug, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc(
            String titleKeyword,
            String contentKeyword,
            Pageable pageable
    );

    List<Post> findTop5ByPinnedTrueOrderByCreatedAtDesc();

    List<Post> findByAuthorUsernameOrderByCreatedAtDesc(String username);
}