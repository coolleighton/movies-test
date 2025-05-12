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

/**
 * This class configures Spring Security for the application.
 * It defines security rules, authentication methods, CORS settings,
 * and other security-related configurations.
 */
@Configuration // Marks this class as a Spring configuration class that defines beans
@EnableWebSecurity // Enables Spring Security web security support
public class SecurityConfig {

  /**
   * Custom implementation of UserDetailsService for authentication.
   * This service loads user-specific data for authentication.
   */
  @Autowired
  private CustomUserDetailsService userDetailsService;

  /**
   * The URL of the frontend application, injected from environment variables.
   * Used for CORS configuration to allow requests from the frontend.
   */
  @Value("${FRONTEND_URL}")
  private String frontendUrl;

  /**
   * Configures the security filter chain that determines how requests are secured.
   * This is the main configuration method for Spring Security.
   *
   * @param http The HttpSecurity object to configure
   * @return The configured SecurityFilterChain
   * @throws Exception If configuration fails
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      // Configure Cross-Origin Resource Sharing (CORS)
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      // Disable Cross-Site Request Forgery (CSRF) protection for API usage
      .csrf(csrf -> csrf.disable())
      // Configure authorization rules for HTTP requests
      .authorizeHttpRequests(auth ->
        auth
          // Public endpoints that don't require authentication
          .requestMatchers("/api/auth/**", "/health")
          .permitAll()
          // Allow OPTIONS preflight requests for CORS
          .requestMatchers(HttpMethod.OPTIONS, "/**")
          .permitAll()
          // All other requests require authentication
          .anyRequest()
          .authenticated()
      )
      // Configure session management
      .sessionManagement(session -> {
        // Always create a session for user tracking
        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        // Mitigate session fixation attacks by changing session ID when user authenticates
        session.sessionFixation().changeSessionId();
      })
      // Configure exception handling
      .exceptionHandling(e ->
        e.authenticationEntryPoint(
          // Return 401 Unauthorized status instead of redirecting when authentication fails
          new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
        )
      )
      // Configure form-based login
      .formLogin(form ->
        form
          // Set the URL for processing login submissions
          .loginProcessingUrl("/api/auth/login")
          // Define custom success handler
          .successHandler((request, response, authentication) -> {
            // Set CORS headers to ensure proper cross-origin response
            response.setHeader("Access-Control-Allow-Origin", frontendUrl);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            // Return 200 OK with success message as JSON
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write("{\"message\": \"Login successful\"}");
            response.setContentType("application/json");
          })
          // Define custom failure handler
          .failureHandler((request, response, exception) -> {
            // Set CORS headers to ensure proper cross-origin response
            response.setHeader("Access-Control-Allow-Origin", frontendUrl);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            // Return 401 Unauthorized with error message as JSON
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response
              .getWriter()
              .write("{\"error\": \"" + exception.getMessage() + "\"}");
            response.setContentType("application/json");
          })
      )
      // Configure logout
      .logout(logout ->
        logout
          // Set the URL for logout requests
          .logoutUrl("/api/auth/logout")
          // Define custom logout success handler
          .logoutSuccessHandler((request, response, authentication) -> {
            // Set CORS headers to ensure proper cross-origin response
            response.setHeader("Access-Control-Allow-Origin", frontendUrl);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            // Return 200 OK with success message as JSON
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write("{\"message\": \"Logout successful\"}");
            response.setContentType("application/json");
          })
          // Allow public access to logout endpoint
          .permitAll()
      );

    // Build and return the configured security filter chain
    return http.build();
  }

  /**
   * Configures CORS (Cross-Origin Resource Sharing) settings.
   * CORS is a security feature implemented by browsers that restricts
   * cross-origin HTTP requests initiated from scripts.
   *
   * @return CorsConfigurationSource with configured CORS rules
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // Allow requests only from the frontend URL
    configuration.setAllowedOrigins(Arrays.asList(frontendUrl));
    // Define allowed HTTP methods
    configuration.setAllowedMethods(
      Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
    );
    // Define allowed HTTP headers
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
    // Define headers exposed to the client
    configuration.setExposedHeaders(
      Arrays.asList(
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials"
      )
    );
    // Allow credentials (cookies, authorization headers, TLS certificates)
    configuration.setAllowCredentials(true);
    // Cache preflight response for 1 hour (3600 seconds)
    configuration.setMaxAge(3600L);

    // Create and configure the CORS source
    UrlBasedCorsConfigurationSource source =
      new UrlBasedCorsConfigurationSource();
    // Apply CORS configuration to all paths
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Registers a custom filter to handle cookie SameSite attribute.
   * SameSite is a cookie attribute that controls when cookies are sent with cross-site requests.
   *
   * @return FilterRegistrationBean for the CookieSameSiteFilter
   */
  @Bean
  public FilterRegistrationBean<CookieSameSiteFilter> cookieSameSiteFilter() {
    FilterRegistrationBean<CookieSameSiteFilter> registration =
      new FilterRegistrationBean<>();
    registration.setFilter(new CookieSameSiteFilter());
    // Apply filter to all requests
    registration.addUrlPatterns("/*");
    // Set highest precedence (executes first)
    registration.setOrder(Integer.MIN_VALUE);
    return registration;
  }

  /**
   * Custom filter class for handling SameSite cookie attribute.
   * Currently, the filter passes requests through without modification,
   * suggesting it may be a placeholder for future implementation.
   */
  public static class CookieSameSiteFilter extends OncePerRequestFilter {

    /**
     * Implementation of the filter logic.
     * Currently only passes the request through without modifications.
     */
    @Override
    protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
    ) throws ServletException, IOException {
      // Simply continue the filter chain without modification
      // This suggests this might be a placeholder for future implementation
      filterChain.doFilter(request, response);
    }
  }

  /**
   * Creates a password encoder bean for securely hashing passwords.
   * BCrypt is a strong adaptive cryptographic hash function designed for password hashing.
   *
   * @return PasswordEncoder implementation using BCrypt
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Creates an authentication provider that uses the custom user details service.
   * This provider validates authentication requests against user details retrieved
   * from the database.
   *
   * @return DaoAuthenticationProvider configured with user details service and password encoder
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    // Set custom UserDetailsService for retrieving user information
    provider.setUserDetailsService(userDetailsService);
    // Set password encoder for validating passwords
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  /**
   * Creates the authentication manager for processing authentication requests.
   * This manager uses the authentication provider defined above.
   *
   * @param authConfig The authentication configuration to use
   * @return AuthenticationManager instance
   * @throws Exception If manager creation fails
   */
  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authConfig
  ) throws Exception {
    return authConfig.getAuthenticationManager();
  }
}
