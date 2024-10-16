package rw.auca.EduSpark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.auca.EduSpark.model.*;
import rw.auca.EduSpark.repository.*;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private EduSparkUserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public MyAppUser registerNewUser(MyAppUser user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MyAppUser savedUser = userRepository.save(user);

        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(roleName.toUpperCase());
                    newRole.setDescription("Role for " + roleName.toLowerCase());
                    return roleRepository.save(newRole);
                });

        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        return savedUser;
    }


    public boolean hasRole(MyAppUser user, String roleName) {
        return user.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getName().equals(roleName.toUpperCase()));
    }

    public List<MyAppUser> getAllUsers() {
        return userRepository.findAll();
    }
    public MyAppUser findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    public void createUse(MyAppUser user) {

        userRepository.save(user);
    }


}