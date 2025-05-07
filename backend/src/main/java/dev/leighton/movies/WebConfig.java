package dev.leighton.movies;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // Allow CORS requests from the frontend running on localhost:5173
    registry
      .addMapping("/api/**") // Apply CORS to all /api/* endpoints
      .allowedOrigins("http://localhost:5173") // Frontend origin
      .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
      .allowedHeaders("*") // Allow all headers
      .allowCredentials(true); // Allow credentials (cookies, HTTP auth)
  }
}
