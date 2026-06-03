package com.example.forum.config;

import com.example.forum.model.Category;
import com.example.forum.model.Comment;
import com.example.forum.model.Post;
import com.example.forum.model.User;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(
                    "General Discussion",
                    "general-discussion",
                    "Nơi thảo luận chung cho tất cả mọi người",
                    "#2563eb"
            ));

            categoryRepository.save(new Category(
                    "Web Development",
                    "web-development",
                    "Thảo luận về HTML, CSS, JavaScript, Spring Boot, Node.js",
                    "#16a34a"
            ));

            categoryRepository.save(new Category(
                    "Study Help",
                    "study-help",
                    "Hỏi đáp bài tập, assignment và project",
                    "#f59e0b"
            ));

            categoryRepository.save(new Category(
                    "Technology News",
                    "technology-news",
                    "Tin tức công nghệ mới nhất",
                    "#7c3aed"
            ));

            categoryRepository.save(new Category(
                    "Off Topic",
                    "off-topic",
                    "Chia sẻ ngoài lề, giải trí và đời sống sinh viên",
                    "#dc2626"
            ));
        }

        if (userRepository.count() == 0) {
            User admin = new User(
                    "admin",
                    "admin@forum.com",
                    passwordEncoder.encode("admin123"),
                    "Forum Administrator",
                    "ROLE_ADMIN"
            );
            admin.setBio("Administrator of Campus Forum.");
            admin.setAvatarUrl("/images/default-avatar.png");
            userRepository.save(admin);

            User student = new User(
                    "student",
                    "student@forum.com",
                    passwordEncoder.encode("student123"),
                    "Demo Student",
                    "ROLE_USER"
            );
            student.setBio("A student who loves web development.");
            student.setAvatarUrl("/images/default-avatar.png");
            userRepository.save(student);
        }

        if (postRepository.count() == 0) {
            User admin = userRepository.findByUsername("admin").orElseThrow();
            User student = userRepository.findByUsername("student").orElseThrow();

            Category general = categoryRepository.findBySlug("general-discussion").orElseThrow();
            Category web = categoryRepository.findBySlug("web-development").orElseThrow();
            Category study = categoryRepository.findBySlug("study-help").orElseThrow();

            Post post1 = new Post(
                    "Welcome to Campus Forum",
                    "welcome-to-campus-forum",
                    "This is the official discussion forum for students. You can ask questions, share knowledge, discuss assignments, and connect with other members.",
                    admin,
                    general
            );
            post1.setPinned(true);
            postRepository.save(post1);

            Post post2 = new Post(
                    "How to start a Spring Boot final project?",
                    "how-to-start-a-spring-boot-final-project",
                    "I am planning to build a web application for my final project. Should I start with database design, UI layout, or authentication first?",
                    student,
                    web
            );
            postRepository.save(post2);

            Post post3 = new Post(
                    "Need help designing database relationships",
                    "need-help-designing-database-relationships",
                    "I want to understand how users, posts, categories, and comments are connected in a forum database. Any suggestions?",
                    student,
                    study
            );
            postRepository.save(post3);

            commentRepository.save(new Comment(
                    "Welcome everyone! Feel free to create discussions and ask questions.",
                    admin,
                    post1
            ));

            commentRepository.save(new Comment(
                    "I think you should start with entity design and database relationships first.",
                    admin,
                    post2
            ));
        }
    }
}