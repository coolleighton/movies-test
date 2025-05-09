package dev.leighton.movies;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * This service class implements the business logic for review-related operations.
 * It handles the creation of reviews and the association of reviews with movies.
 */
@Service // Marks this class as a Spring Service component to be detected during component scanning
public class ReviewService {

  /**
   * Repository interface for accessing and manipulating Review entities in the database.
   * Autowired to inject the implementation provided by Spring Boot.
   */
  @Autowired
  private ReviewRepository repository;

  /**
   * MongoTemplate provides more flexible and lower-level access to MongoDB than repositories.
   * It's used here for complex operations that can't be easily achieved with repository methods.
   * Specifically, it's used to update a Movie document by adding a review reference to it.
   */
  @Autowired
  private MongoTemplate mongoTemplate;

  /**
   * Creates a new review and associates it with a movie.
   * This method performs two database operations:
   * 1. Inserts a new Review document
   * 2. Updates the associated Movie document to reference the new review
   *
   * @param reviewBody The text content of the review
   * @param imdbId The IMDb ID of the movie to associate the review with
   * @return The created Review entity, including its generated ID
   */
  public Review createReview(String reviewBody, String imdbId) {
    // Create a new Review object with current timestamps for both created and updated fields
    Review review = new Review(
      reviewBody,
      LocalDateTime.now(),
      LocalDateTime.now()
    );

    // Insert the review into the database and get the saved entity with its generated ID
    // Note: Using insert() instead of save() to ensure this is a new document creation
    review = repository.insert(review);

    // Update the Movie document to add a reference to this review
    // This demonstrates a MongoDB-specific approach to handling relationships
    mongoTemplate
      .update(Movie.class) // Specify the document type to update
      .matching(Criteria.where("imdbId").is(imdbId)) // Find the movie by imdbId
      .apply(new Update().push("reviewIds").value(review.getId())) // Add review ID to the reviewIds array
      .first(); // Update only the first matching document (should be only one)

    // Return the created review
    return review;
  }
}
