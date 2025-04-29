package dev.leighton.movies;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
      .authorizeHttpRequests(
        auth ->
          auth
            .requestMatchers("/api/auth/**")
            .permitAll() // Allow open access to authentication routes
            .anyRequest()
            .authenticated() // All other requests need authentication
      )
      .httpBasic(httpBasic -> {}) // Configure HTTP Basic authentication with empty lambda
      .formLogin(
        form -> form.loginPage("/login").permitAll() // Enable custom login page
      )
      .logout(
        logout -> logout.logoutUrl("/logout").permitAll() // Configure logout endpoint
      );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // Use BCrypt password encoding
  }
}
