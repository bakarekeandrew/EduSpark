package rw.auca.EduSpark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rw.auca.EduSpark.model.EduSparkUserRepository;
import rw.auca.EduSpark.model.MyAppUser;
import rw.auca.EduSpark.service.UserService;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private EduSparkUserRepository eduSparkUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/req/signup")
    public ResponseEntity<String> createUser(@ModelAttribute MyAppUser user, @RequestParam(defaultValue = "STUDENT") String role) {
        // Encode the password before saving
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userService.registerNewUser(user, "STUDENT");
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
}