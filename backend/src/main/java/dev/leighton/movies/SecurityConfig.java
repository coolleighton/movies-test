package dev.leighton.movies;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
          .requestMatchers("/api/auth/**", "/health")
          .permitAll()
          .requestMatchers(HttpMethod.OPTIONS, "/**")
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .sessionManagement(session -> {
        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        session.sessionFixation().changeSessionId();
      })
      .exceptionHandling(e ->
        e.authenticationEntryPoint(
          new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
        )
      )
      .formLogin(form ->
        form
          .loginProcessingUrl("/api/auth/login")
          .successHandler((request, response, authentication) -> {
            // Set CORS headers
            response.setHeader("Access-Control-Allow-Origin", frontendUrl);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            // Log session ID for debugging
            System.out.println(
              "Login successful, Session ID: " + request.getSession().getId()
            );

            // Explicitly set the session cookie with appropriate attributes
            // Add SameSite=None via header since standard Cookie class doesn't support it
            response.addHeader(
              "Set-Cookie",
              String.format(
                "JSESSIONID=%s; Path=/; HttpOnly; SameSite=None; Secure",
                request.getSession().getId()
              )
            );

            // Set session max inactive interval (30 minutes)
            request.getSession().setMaxInactiveInterval(1800);

            // Return success response
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write("{\"message\": \"Login successful\"}");
            response.setContentType("application/json");
          })
          .failureHandler((request, response, exception) -> {
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
    // Allow both production and development frontend URLs
    configuration.setAllowedOrigins(
      Arrays.asList(frontendUrl, "http://localhost:5173")
    );
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
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source =
      new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public FilterRegistrationBean<CookieSameSiteFilter> cookieSameSiteFilter() {
    FilterRegistrationBean<CookieSameSiteFilter> registration =
      new FilterRegistrationBean<>();
    registration.setFilter(new CookieSameSiteFilter());
    registration.addUrlPatterns("/*");
    registration.setOrder(Integer.MIN_VALUE);
    return registration;
  }

  public static class CookieSameSiteFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
    ) throws ServletException, IOException {
      // Store the session ID before proceeding
      String sessionId = request.getSession().getId();
      boolean isNewSession = request.getSession().isNew();

      System.out.println("=== Filter for: " + request.getRequestURI() + " ===");
      System.out.println("Session ID: " + sessionId);
      System.out.println("Is new session: " + isNewSession);

      // Print incoming cookies
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        System.out.println("Incoming cookies:");
        for (Cookie cookie : cookies) {
          System.out.println(
            "  " + cookie.getName() + ": " + cookie.getValue()
          );
        }
      } else {
        System.out.println("No cookies received");
      }

      // Force set JSESSIONID cookie before filter chain
      response.addHeader(
        "Set-Cookie",
        String.format(
          "JSESSIONID=%s; Path=/; Domain=.fly.dev; HttpOnly; SameSite=None; Secure",
          sessionId
        )
      );

      // Continue with the filter chain
      filterChain.doFilter(request, response);

      // Force set JSESSIONID cookie after filter chain as well
      response.addHeader(
        "Set-Cookie",
        String.format(
          "JSESSIONID=%s; Path=/; Domain=.fly.dev; HttpOnly; SameSite=None; Secure",
          sessionId
        )
      );

      System.out.println("Added JSESSIONID cookie to response: " + sessionId);
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
