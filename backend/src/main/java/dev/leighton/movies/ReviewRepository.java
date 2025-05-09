package dev.leighton.movies;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface defines the data access operations for Review entities.
 * It extends MongoRepository to inherit standard CRUD operations for MongoDB documents,
 * without defining any additional custom query methods.
 */
@Repository // Marks this interface as a Spring Repository component to be detected during component scanning
public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
  /**
   * This repository inherits all standard data access methods from MongoRepository:
   *
   * - save(Review) - Saves a Review entity to the database (insert or update)
   * - findById(ObjectId) - Finds a Review by its ID
   * - findAll() - Retrieves all Review documents
   * - count() - Returns the number of Review documents
   * - delete(Review) - Deletes a Review entity
   * - deleteById(ObjectId) - Deletes a Review by its ID
   * - existsById(ObjectId) - Checks if a Review with the given ID exists
   *
   * Unlike MovieRepository, this interface doesn't define any custom query methods,
   * indicating that the standard CRUD operations are sufficient for the application's
   * review management requirements.
   *
   * If needed, custom query methods could be added following Spring Data's method
   * naming conventions, such as:
   * - findByCreatedAfter(LocalDateTime date)
   * - findByBodyContaining(String text)
   */
}
