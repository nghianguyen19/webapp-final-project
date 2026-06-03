package com.example.forum.service;

import com.example.forum.model.Comment;
import com.example.forum.model.Post;
import com.example.forum.model.Report;
import com.example.forum.model.User;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public ReportService(
            ReportRepository reportRepository,
            UserRepository userRepository,
            PostRepository postRepository,
            CommentRepository commentRepository
    ) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public void reportPost(Long postId, String username, String reason, String description) {
        User reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Report report = new Report(reason, description, reporter, post, null);

        reportRepository.save(report);
    }

    public void reportComment(Long commentId, String username, String reason, String description) {
        User reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Report report = new Report(reason, description, reporter, null, comment);

        reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc();
    }

    public void markReviewed(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus("REVIEWED");
        reportRepository.save(report);
    }

    public void deleteReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        reportRepository.delete(report);
    }

    public long countPendingReports() {
        return reportRepository.countByStatus("PENDING");
    }
}