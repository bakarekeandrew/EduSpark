package rw.auca.EduSpark.Security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import rw.auca.EduSpark.model.*;
import rw.auca.EduSpark.repository.*;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(EduSparkUserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role newAdminRole = new Role();
                        newAdminRole.setName("ROLE_ADMIN");
                        return roleRepository.save(newAdminRole);
                    });

            if (userRepository.findByUsername("admin").isEmpty()) {
                MyAppUser adminUser = new MyAppUser();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("adminpass"));
                adminUser.setEmail("admin@example.com");
                adminUser.setFirstName("Admin");
                adminUser.setLastName("User");
                adminUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
                adminUser.setPhoneNumber("1234567890");

                adminUser.addRole(adminRole);

                userRepository.save(adminUser);
            }
        };
    }
}