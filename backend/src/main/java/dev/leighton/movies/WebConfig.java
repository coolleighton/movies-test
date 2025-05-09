package dev.leighton.movies;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This configuration class customizes Spring MVC's behavior for the application.
 * It specifically focuses on configuring Cross-Origin Resource Sharing (CORS)
 * at the MVC level, complementing the CORS settings in SecurityConfig.
 */
@Configuration // Marks this class as a Spring configuration class
public class WebConfig implements WebMvcConfigurer {

  /**
   * Configures Cross-Origin Resource Sharing (CORS) settings for the application.
   * This method overrides the default implementation in WebMvcConfigurer to add
   * custom CORS mappings for API endpoints.
   *
   * CORS is a security feature implemented by browsers that restricts cross-origin
   * HTTP requests initiated from scripts. This configuration allows the frontend
   * application running on a different origin to access the backend API.
   *
   * Note: This configuration works alongside the CORS configuration in SecurityConfig.
   * In a properly configured application, both should have consistent settings.
   *
   * @param registry The CorsRegistry to add mappings to
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // Configure CORS for all API endpoints
    registry
      .addMapping("/api/**") // Apply CORS to all paths starting with /api/
      .allowedOrigins("${FRONTEND_URL}") // Allow requests from the frontend URL
      // The ${FRONTEND_URL} placeholder will be replaced with
      // the actual value from application properties
      .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allowed HTTP methods
      // Notably, OPTIONS is not explicitly included,
      // though browsers send OPTIONS as preflight requests
      .allowedHeaders("*") // Allow all request headers
      // This is more permissive than the SecurityConfig CORS settings
      .allowCredentials(true); // Allow sending cookies and authentication headers
    // Essential for session-based authentication
  }
}
