package dev.leighton.movies;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface defines the data access operations for Movie entities.
 * It extends MongoRepository to inherit standard CRUD operations for MongoDB documents,
 * while also defining custom query methods specific to the Movie entity.
 */
@Repository // Marks this interface as a Spring Repository component to be detected during component scanning
public interface MovieRepository extends MongoRepository<Movie, ObjectId> {
  /**
   * Custom query method to find a movie by its IMDb ID.
   * Spring Data MongoDB automatically implements this method based on its name,
   * following the naming convention: findEntityByFieldName.
   *
   * The method searches for a Movie document where the imdbId field matches
   * the provided parameter.
   *
   * @param imdbId The IMDb ID to search for in the movie collection
   * @return Optional<Movie> containing the movie if found, or an empty Optional if not found.
   *         Using Optional eliminates the need for null checks and follows modern Java conventions.
   */
  Optional<Movie> findMovieByImdbId(String imdbId);
}
