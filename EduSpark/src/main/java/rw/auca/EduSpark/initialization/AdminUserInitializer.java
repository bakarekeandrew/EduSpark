package rw.auca.EduSpark.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rw.auca.EduSpark.model.MyAppUser;
import rw.auca.EduSpark.service.UserService;

import java.time.LocalDate;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (userService.findByUsername("admin") == null) {
            MyAppUser adminUser = new MyAppUser();
            adminUser.setUsername("admin");
            adminUser.setPassword("localhost"); // This will be encoded by UserService
            adminUser.setEmail("admin@gmail.com");
            adminUser.setFirstName("admin");
            adminUser.setLastName("Owner");
            adminUser.setPhoneNumber("0789490560");
            adminUser.setDateOfBirth(LocalDate.parse("2023-10-15"));
            userService.registerNewUser(adminUser, "ADMIN");
        }
    }
}