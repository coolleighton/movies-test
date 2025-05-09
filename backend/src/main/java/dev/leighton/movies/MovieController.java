package dev.leighton.movies;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles all HTTP requests related to movie operations.
 * It provides endpoints for retrieving movie data through the REST API.
 */
@RestController // Marks this class as a REST controller to handle HTTP requests
@RequestMapping("/api/v1/movies") // Base URL path for all endpoints in this controller
public class MovieController {

  /**
   * Service that contains the business logic for movie operations.
   * Autowired to inject the implementation provided by Spring Boot.
   * This follows the dependency injection pattern to decouple the controller from the service implementation.
   */
  @Autowired
  private MovieService service;

  /**
   * Endpoint to retrieve all movies.
   * Maps to HTTP GET requests at the base path (/api/v1/movies).
   *
   * @return ResponseEntity containing a list of all movies and HTTP status 200 (OK)
   */
  @GetMapping
  public ResponseEntity<List<Movie>> getMovies() {
    // Call the service layer to retrieve all movies from the database
    List<Movie> allMovies = service.findAllMovies();

    // Return the list of movies wrapped in a ResponseEntity with HTTP 200 OK status
    return new ResponseEntity<List<Movie>>(allMovies, HttpStatus.OK);
  }

  /**
   * Endpoint to retrieve a single movie by its IMDb ID.
   * Maps to HTTP GET requests at the path /api/v1/movies/{imdbId}.
   *
   * @param imdbId The IMDb ID of the movie to retrieve, extracted from the URL path
   * @return ResponseEntity containing the movie (if found) and HTTP status 200 (OK)
   *         Optional is used to handle cases where the movie might not exist
   */
  @GetMapping("/{imdbId}")
  public ResponseEntity<Optional<Movie>> getSingleMovie(
    @PathVariable String imdbId // Extract the imdbId value from the URL path
  ) {
    // Call the service layer to find a specific movie by its IMDb ID
    Optional<Movie> movie = service.findMovieByImdbId(imdbId);

    // Return the movie wrapped in a ResponseEntity with HTTP 200 OK status
    // Note that if the movie is not found, an empty Optional will be returned
    // (the controller does not handle the not-found case with a different status code)
    return new ResponseEntity<Optional<Movie>>(movie, HttpStatus.OK);
  }
}
