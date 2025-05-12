package dev.leighton.movies;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

  @Autowired
  private ReviewRepository repository;

  @Autowired
  private MongoTemplate mongoTemplate;

  public Review createReview(String reviewBody, String imdbId) {
    // Create a new Review object with current timestamps for both created and updated fields
    Review review = new Review(
      reviewBody,
      LocalDateTime.now(),
      LocalDateTime.now()
    );

    // Insert the review into the database and get the saved entity with its generated ID

    review = repository.insert(review);

    // Update the Movie document to add a reference to this review

    mongoTemplate
      .update(Movie.class)
      .matching(Criteria.where("imdbId").is(imdbId))
      .apply(new Update().push("reviewIds").value(review.getId()))
      .first();

    // Return the created review
    return review;
  }
}
