package rw.auca.EduSpark.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .authorizeHttpRequests((requests) -> requests
                      .requestMatchers("/", "/home", "/req/signup").permitAll()
                      .requestMatchers("/admin/**").hasRole("ADMIN")
                      .requestMatchers("/teacher/**").hasRole("TEACHER")
                      .requestMatchers("/student/**").hasRole("STUDENT")
                      .anyRequest().authenticated()
              )
              .formLogin((form) -> form
                      .loginPage("/login")
                      .loginProcessingUrl("/login")  // This should match your @PostMapping("/login") in the controller
                      .permitAll()
              )
              .logout((logout) -> logout.permitAll());

      return http.build();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
   }
}