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


    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Transactional
    public MyAppUser addRoleToUser(MyAppUser user, String roleName) {
        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        return user;
    }


    public MyAppUser findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
    //add gerUserById
    public List<MyAppUser> getAllUsersWithRoles() {
        List<MyAppUser> users = userRepository.findAll();
        for (MyAppUser user : users) {
            user.getUserRoles().size();
        }
        return users;
    }
    public MyAppUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    @Transactional
    public MyAppUser updateUser(Long id, MyAppUser updatedUser, String roleName) {
        MyAppUser existingUser = getUserById(id);

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        if (!updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        MyAppUser savedUser = userRepository.save(existingUser);

        // Update role if changed
        Role newRole = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        UserRole existingUserRole = userRoleRepository.findByUser(savedUser).get(0);
        if (!existingUserRole.getRole().equals(newRole)) {
            existingUserRole.setRole(newRole);
            userRoleRepository.save(existingUserRole);
        }

        return savedUser;
    }


    public boolean hasRole(MyAppUser user, String roleName) {
        return user.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getName().equals(roleName.toUpperCase()));
    }

    public List<MyAppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long id) {
        MyAppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Manually delete user roles
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        userRoleRepository.deleteAll(userRoles);

        // Delete the user
        userRepository.delete(user);
    }
}