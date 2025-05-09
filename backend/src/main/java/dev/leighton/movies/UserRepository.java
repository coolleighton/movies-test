package dev.leighton.movies;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This interface defines the data access operations for User entities.
 * It extends MongoRepository to inherit standard CRUD operations for MongoDB documents,
 * while also defining a custom query method specific to user authentication.
 *
 * Unlike other repositories in this application, this interface doesn't use the @Repository
 * annotation. Spring Data MongoDB still detects and implements it because it extends MongoRepository.
 */
public interface UserRepository extends MongoRepository<User, String> {
  /**
   * Custom query method to find a user by username.
   * Spring Data MongoDB automatically implements this method based on its name,
   * following the naming convention: findByFieldName.
   *
   * This method is critical for the authentication process, as it's used by
   * CustomUserDetailsService to load user details during login.
   *
   * Unlike similar methods in other repositories, this returns User directly instead of Optional<User>.
   * This means it will return null if no user is found with the given username,
   * which is handled in the CustomUserDetailsService by throwing UsernameNotFoundException.
   *
   * @param username The username to search for in the users collection
   * @return The User if found, or null if not found
   */
  User findByUsername(String username);
  /**
   * This repository inherits all standard data access methods from MongoRepository:
   *
   * - save(User) - Saves a User entity to the database (insert or update)
   * - findById(String) - Finds a User by its ID
   * - findAll() - Retrieves all User documents
   * - count() - Returns the number of User documents
   * - delete(User) - Deletes a User entity
   * - deleteById(String) - Deletes a User by its ID
   * - existsById(String) - Checks if a User with the given ID exists
   */
}
