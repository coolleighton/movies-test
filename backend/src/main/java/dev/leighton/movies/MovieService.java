package dev.leighton.movies;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service class implements the business logic for movie-related operations.
 * It serves as an intermediary layer between the controller (presentation layer)
 * and the repository (data access layer), providing an abstraction over the data access operations.
 */
@Service // Marks this class as a Spring Service component to be detected during component scanning
public class MovieService {

  /**
   * Repository interface for accessing and manipulating Movie entities in the database.
   * Autowired to inject the implementation provided by Spring Boot.
   * This follows the dependency injection pattern to decouple the service from repository implementation.
   */
  @Autowired
  private MovieRepository repository;

  /**
   * Retrieves all movies from the database.
   * This method delegates to the repository's findAll method, which is provided
   * by Spring Data's MongoRepository interface.
   *
   * @return List<Movie> containing all movies stored in the database
   */
  public List<Movie> findAllMovies() {
    // Simply delegates to the repository's findAll method
    return repository.findAll();
  }

  /**
   * Finds a specific movie by its IMDb ID.
   * This method delegates to the custom repository method findMovieByImdbId.
   *
   * @param imdbId The IMDb ID of the movie to find
   * @return Optional<Movie> containing the movie if found, or an empty Optional if not found.
   *         Using Optional eliminates the need for null checks and follows modern Java conventions.
   */
  public Optional<Movie> findMovieByImdbId(String imdbId) {
    // Delegates to the repository's custom query method
    return repository.findMovieByImdbId(imdbId);
  }
}
