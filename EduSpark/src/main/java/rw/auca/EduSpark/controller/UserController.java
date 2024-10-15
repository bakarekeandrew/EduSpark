package rw.auca.EduSpark.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentDashboard() {
        return "index";
    }

//    @GetMapping("/teacher")
//    @PreAuthorize("hasRole('TEACHER')")
//    public String teacherDashboard() {
//        return "teacher-dashboard";
//    }

//    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String adminDashboard() {
//        return "admin-dashboard";
//    }
}