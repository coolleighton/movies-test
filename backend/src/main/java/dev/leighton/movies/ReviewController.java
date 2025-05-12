package dev.leighton.movies;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

  @Autowired
  private ReviewService service;

  @PostMapping
  public ResponseEntity<Review> createReview(
    @RequestBody Map<String, String> payload
  ) {
    String reviewBody = payload.get("reviewBody");
    String imdbId = payload.get("imdbId");

    Review createdReview = service.createReview(reviewBody, imdbId);

    return new ResponseEntity<Review>(createdReview, HttpStatus.OK);
  }
}
