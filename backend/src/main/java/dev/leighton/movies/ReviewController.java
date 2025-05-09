package dev.leighton.movies;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles all HTTP requests related to review operations.
 * It provides endpoints for creating and managing movie reviews through the REST API.
 */
@RestController // Marks this class as a REST controller to handle HTTP requests
@RequestMapping("/api/v1/reviews") // Base URL path for all endpoints in this controller
public class ReviewController {

  /**
   * Service that contains the business logic for review operations.
   * Autowired to inject the implementation provided by Spring Boot.
   * This follows the dependency injection pattern to decouple the controller from the service implementation.
   */
  @Autowired
  private ReviewService service;

  /**
   * Endpoint to create a new movie review.
   * Maps to HTTP POST requests at the base path (/api/v1/reviews).
   *
   * @param payload A map containing the review data from the request body.
   *                Expected to contain "reviewBody" (the text of the review) and
   *                "imdbId" (the IMDb ID of the movie being reviewed).
   *                Using Map<String, String> allows for flexible request structure without
   *                requiring a dedicated request DTO class.
   * @return ResponseEntity containing the created Review object and HTTP status 200 (OK)
   */
  @PostMapping
  public ResponseEntity<Review> createReview(
    @RequestBody Map<String, String> payload // Extract data from the JSON request body
  ) {
    // Extract values from the payload map
    String reviewBody = payload.get("reviewBody"); // The text content of the review
    String imdbId = payload.get("imdbId"); // The IMDb ID of the movie to review

    // Call the service layer to create a new review and associate it with the specified movie
    Review createdReview = service.createReview(reviewBody, imdbId);

    // Return the created review wrapped in a ResponseEntity with HTTP 200 OK status
    return new ResponseEntity<Review>(createdReview, HttpStatus.OK);
    // Note: HTTP 201 CREATED would be more appropriate for a resource creation operation,
    // but this controller uses 200 OK for consistency with the rest of the API.
  }
}
