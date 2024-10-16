package rw.auca.EduSpark.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rw.auca.EduSpark.model.MyAppUser;
import rw.auca.EduSpark.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
public class ContentController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    @Autowired
    public ContentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, Model model) {
        // Dummy login logic, just redirect for now
        logger.info("Login attempt for user: {}", username);
        return "redirect:/home";
    }

    @GetMapping("/req/signup")
    public String signup() {
        return "Signup";
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        logger.info("Accessing admin dashboard");
        List<MyAppUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    @GetMapping("/teacher")
    public String teacherDashboard() {
        return "teacher-dashboard";
    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        // Create a blank user object to bind form data
        model.addAttribute("user", new MyAppUser());
        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") MyAppUser user, RedirectAttributes redirectAttributes) {
        try {
            String roleName = "STUDENT";
            userService.registerNewUser(user, roleName);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully.");
        } catch (Exception e) {
            logger.error("Error creating user", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create user.");
        }
        return "redirect:/admin";
    }

}
