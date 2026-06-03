package com.example.forum.controller;

import com.example.forum.model.Post;
import com.example.forum.model.RoleDefinition;
import com.example.forum.model.User;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.UserRepository;
import com.example.forum.service.AdminService;
import com.example.forum.service.ReportService;
import com.example.forum.service.RoleDefinitionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final ReportService reportService;
    private final RoleDefinitionService roleDefinitionService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

    public AdminController(
            AdminService adminService,
            ReportService reportService,
            RoleDefinitionService roleDefinitionService,
            UserRepository userRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            CategoryRepository categoryRepository
    ) {
        this.adminService = adminService;
        this.reportService = reportService;
        this.roleDefinitionService = roleDefinitionService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        List<Post> recentPosts = postRepository.findAllByOrderByCreatedAtDesc();

        if (recentPosts.size() > 5) {
            recentPosts = recentPosts.subList(0, 5);
        }

        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalComments", commentRepository.count());
        model.addAttribute("totalCategories", categoryRepository.count());
        model.addAttribute("adminCount", userRepository.countByRole("ROLE_ADMIN"));
        model.addAttribute("userCount", userRepository.countByRole("ROLE_USER"));
        model.addAttribute("pendingReports", reportService.countPendingReports());
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("roleLabels", roleDefinitionService.getRoleLabelMap());

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("pageTitle", "Manage Users");
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("roles", roleDefinitionService.getAllRoles());
        model.addAttribute("roleLabels", roleDefinitionService.getRoleLabelMap());

        return "admin/users";
    }

    @PostMapping("/users/toggle-status/{id}")
    public String toggleUserStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("admin".equalsIgnoreCase(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "The main admin account cannot be locked.");
            return "redirect:/admin/users";
        }

        adminService.toggleUserStatus(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/make-admin/{id}")
    public String makeAdmin(@PathVariable Long id) {
        adminService.makeAdmin(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/make-user/{id}")
    public String makeUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("admin".equalsIgnoreCase(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "The main admin account cannot be demoted.");
            return "redirect:/admin/users";
        }

        adminService.makeUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/change-role/{id}")
    public String changeUserRole(
            @PathVariable Long id,
            @RequestParam String roleCode,
            RedirectAttributes redirectAttributes
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("admin".equalsIgnoreCase(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "The main admin account role cannot be changed.");
            return "redirect:/admin/users";
        }

        RoleDefinition roleDefinition = roleDefinitionService.getRoleByCode(roleCode);

        user.setRole(roleDefinition.getRoleCode());
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "User role updated successfully.");
        return "redirect:/admin/users";
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("pageTitle", "Manage Posts");
        model.addAttribute("posts", adminService.getAllPosts());

        return "admin/posts";
    }

    @PostMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
        return "redirect:/admin/posts";
    }

    @PostMapping("/posts/toggle-pin/{id}")
    public String togglePostPin(@PathVariable Long id) {
        adminService.togglePostPin(id);
        return "redirect:/admin/posts";
    }

    @PostMapping("/posts/toggle-lock/{id}")
    public String togglePostLock(@PathVariable Long id) {
        adminService.togglePostLock(id);
        return "redirect:/admin/posts";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("pageTitle", "Manage Categories");
        model.addAttribute("categories", adminService.getAllCategories());

        return "admin/categories";
    }

    @PostMapping("/categories")
    public String createCategory(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(defaultValue = "#2563eb") String color,
            Model model
    ) {
        try {
            adminService.createCategory(name, description, color);
            return "redirect:/admin/categories";
        } catch (RuntimeException e) {
            model.addAttribute("pageTitle", "Manage Categories");
            model.addAttribute("categories", adminService.getAllCategories());
            model.addAttribute("error", e.getMessage());
            return "admin/categories";
        }
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("pageTitle", "Manage Reports");
        model.addAttribute("reports", reportService.getAllReports());

        return "admin/reports";
    }

    @PostMapping("/reports/review/{id}")
    public String markReportReviewed(@PathVariable Long id) {
        reportService.markReviewed(id);
        return "redirect:/admin/reports";
    }

    @PostMapping("/reports/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/admin/reports";
    }

    @GetMapping("/roles")
    public String roles(Model model) {
        model.addAttribute("pageTitle", "Manage Roles");
        model.addAttribute("roles", roleDefinitionService.getAllRoles());

        return "admin/roles";
    }

    @PostMapping("/roles")
    public String createRole(
            @RequestParam String roleCode,
            @RequestParam String displayName,
            @RequestParam(required = false) String description,
            Model model
    ) {
        try {
            roleDefinitionService.createRole(roleCode, displayName, description);
            return "redirect:/admin/roles";
        } catch (RuntimeException e) {
            model.addAttribute("pageTitle", "Manage Roles");
            model.addAttribute("roles", roleDefinitionService.getAllRoles());
            model.addAttribute("error", e.getMessage());
            return "admin/roles";
        }
    }

    @PostMapping("/roles/update/{id}")
    public String updateRole(
            @PathVariable Long id,
            @RequestParam String displayName,
            @RequestParam(required = false) String description,
            Model model
    ) {
        try {
            roleDefinitionService.updateRole(id, displayName, description);
            return "redirect:/admin/roles";
        } catch (RuntimeException e) {
            model.addAttribute("pageTitle", "Manage Roles");
            model.addAttribute("roles", roleDefinitionService.getAllRoles());
            model.addAttribute("error", e.getMessage());
            return "admin/roles";
        }
    }

    @PostMapping("/roles/delete/{id}")
    public String deleteRole(
            @PathVariable Long id,
            Model model
    ) {
        try {
            roleDefinitionService.deleteRole(id);
            return "redirect:/admin/roles";
        } catch (RuntimeException e) {
            model.addAttribute("pageTitle", "Manage Roles");
            model.addAttribute("roles", roleDefinitionService.getAllRoles());
            model.addAttribute("error", e.getMessage());
            return "admin/roles";
        }
    }
}