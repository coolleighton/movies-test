package dev.leighton.movies;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Value("${FRONTEND_URL}")
  private String frontendUrl;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers("/api/auth/**")
          .permitAll()
          // Allow OPTIONS preflight requests
          .requestMatchers(HttpMethod.OPTIONS, "/**")
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      // Configure session management - use stateful sessions with specific properties
      .sessionManagement(session -> {
        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        // Add this to help with session fixation issues
        session.sessionFixation().changeSessionId();
      })
      // Return 401 instead of redirecting for unauthenticated API requests
      .exceptionHandling(e ->
        e.authenticationEntryPoint(
          new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
        )
      )
      // Configure form login for API authentication
      .formLogin(form ->
        form
          .loginProcessingUrl("/api/auth/login")
          .successHandler((request, response, authentication) -> {
            // Set CORS headers to ensure proper cross-origin response
            response.setHeader("Access-Control-Allow-Origin", frontendUrl);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write("{\"message\": \"Login successful\"}");
            response.setContentType("application/json");
          })
          .failureHandler((request, response, exception) -> {
            // Set CORS headers to ensure proper cross-origin response
            response.setHeader("Access-Control-Allow-Origin", frontendUrl);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response
              .getWriter()
              .write("{\"error\": \"" + exception.getMessage() + "\"}");
            response.setContentType("application/json");
          })
      )
      .logout(logout ->
        logout
          .logoutUrl("/api/auth/logout")
          .logoutSuccessHandler((request, response, authentication) -> {
            // Set CORS headers to ensure proper cross-origin response
            response.setHeader("Access-Control-Allow-Origin", frontendUrl);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write("{\"message\": \"Logout successful\"}");
            response.setContentType("application/json");
          })
          .permitAll()
      );

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // Use setAllowedOriginPatterns instead of setAllowedOrigins for wildcard support
    configuration.setAllowedOrigins(Arrays.asList(frontendUrl));
    configuration.setAllowedMethods(
      Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
    );
    configuration.setAllowedHeaders(
      Arrays.asList(
        "Authorization",
        "Content-Type",
        "X-Requested-With",
        "Accept",
        "Origin",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers"
      )
    );
    configuration.setExposedHeaders(
      Arrays.asList(
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials"
      )
    );
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L); // 1 hour cache for preflight requests

    UrlBasedCorsConfigurationSource source =
      new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  // Custom filter to set SameSite attribute to None
  @Bean
  public FilterRegistrationBean<CookieSameSiteFilter> cookieSameSiteFilter() {
    FilterRegistrationBean<CookieSameSiteFilter> registration =
      new FilterRegistrationBean<>();
    registration.setFilter(new CookieSameSiteFilter());
    registration.addUrlPatterns("/*");
    registration.setOrder(Integer.MIN_VALUE);
    return registration;
  }

  // Custom filter class
  public static class CookieSameSiteFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
    ) throws ServletException, IOException {
      filterChain.doFilter(request, response);
    }
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authConfig
  ) throws Exception {
    return authConfig.getAuthenticationManager();
  }
}
