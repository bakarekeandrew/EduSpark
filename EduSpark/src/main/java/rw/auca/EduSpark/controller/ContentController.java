package rw.auca.EduSpark.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rw.auca.EduSpark.model.MyAppUser;
import rw.auca.EduSpark.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Controller
public class ContentController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "adminpass";
    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    @Autowired
    public ContentController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
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
    public String processLogin(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        logger.info("Login attempt for user: {}", username);

        try {
            Authentication authentication;
            if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
                logger.info("Admin credentials matched");
                // For admin, create a custom authentication with ADMIN role
                authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        password,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return "redirect:/admin";
            } else {
                // For non-admin users, use the authenticationManager
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TEACHER"))) {
                    return "redirect:/teacher";
                } else {
                    return "redirect:/home";
                }
            }
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", username, e);
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/req/signup")
    public String signup() {
        return "Signup";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewAllUsers(Model model) {
        List<MyAppUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "allUsers";
    }

    @GetMapping("/admin/users/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUserForm(Model model) {
        model.addAttribute("user", new MyAppUser());
        return "createUser";
    }

    @PostMapping("/admin/users/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(@ModelAttribute MyAppUser user, @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            userService.registerNewUser(user, role);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editUserForm(@PathVariable Long id, Model model) {
        MyAppUser user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/admin/users/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editUser(@PathVariable Long id, @ModelAttribute MyAppUser user, @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, user, role);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Authentication authentication) {
        logger.info("Accessing admin dashboard");
        if (authentication != null) {
            logger.info("User roles: {}", authentication.getAuthorities());
        }
        return "admin-dashboard";
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public String teacherDashboard() {
        return "teacher-dashboard";
    }
    @GetMapping("/create")
//    @PreAuthorize("hasRole('TEACHER')")
    public String create() {
        return "create";
    }

    @PostMapping("/admin/assign-role")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignRole(@RequestParam String username, @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            MyAppUser user = userService.findUserByUsername(username);
            if (user != null) {
                userService.addRoleToUser(user, role);
                redirectAttributes.addFlashAttribute("successMessage", "Role assigned successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
            }
        } catch (Exception e) {
            logger.error("Error assigning role to user: {}", username, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error assigning role: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}