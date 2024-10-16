package rw.auca.EduSpark.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import rw.auca.EduSpark.model.MyAppUser;
import rw.auca.EduSpark.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

//   @Autowired
//   private UserDetailsService userDetailsService;
//
//   @Bean
//   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//      http
//              .authorizeHttpRequests(authorize -> authorize
//                      .requestMatchers("/", "/home", "/req/signup").permitAll()
//                      .requestMatchers("/admin").hasRole("ADMIN")
//                      .requestMatchers("/teacher/**").hasRole("TEACHER")
//                      .anyRequest().authenticated()
//              )
//              .formLogin(form -> form
//                      .loginPage("/login")
//                      .loginProcessingUrl("/login")
//                      .permitAll()
//              )
//              .logout(logout -> logout
//                      .permitAll()
//              );
//
//      return http.build();
//   }
//
//   @Bean
//   public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//      return authConfig.getAuthenticationManager();
//   }
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
