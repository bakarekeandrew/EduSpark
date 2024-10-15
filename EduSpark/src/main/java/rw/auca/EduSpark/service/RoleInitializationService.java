package rw.auca.EduSpark.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.auca.EduSpark.model.*;
import rw.auca.EduSpark.repository.RoleRepository;

@Service
public class RoleInitializationService {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    @Transactional
    public void initializeRoles() {
        if (roleRepository.count() == 0) {
            createRoleIfNotFound("STUDENT", "Default role for students");
            createRoleIfNotFound("TEACHER", "Role for teachers");
            createRoleIfNotFound("ADMIN", "Administrative role");
        }
    }

    @Transactional
    public Role createRoleIfNotFound(String name, String description) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setDescription(description);
                    return roleRepository.save(role);
                });
    }
}