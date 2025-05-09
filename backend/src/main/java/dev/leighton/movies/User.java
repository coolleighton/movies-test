package dev.leighton.movies;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class represents a User entity in the application.
 * It defines the data structure for user accounts and maps to documents
 * in the "users" collection in MongoDB.
 * It stores essential user information needed for authentication and identification.
 */
@Document(collection = "users") // Specifies the MongoDB collection name this class maps to
public class User {

  /**
   * The primary key for the user document in MongoDB.
   * Unlike other entities that use ObjectId, this uses String which is more flexible
   * and can support various ID generation strategies.
   */
  @Id // Marks this field as the primary identifier for MongoDB documents
  private String id;

  /**
   * The username used for authentication and user identification.
   * This should be unique across all users in the system.
   * Used during login process to identify the user account.
   */
  private String username;

  /**
   * The user's password, stored in hashed form for security.
   * Raw passwords should never be stored in the database.
   * The @PasswordEncoder configured in SecurityConfig is responsible for
   * hashing this password before storage and verifying it during authentication.
   */
  private String password; // (stored hashed)

  /**
   * Default no-argument constructor required by Spring Data MongoDB.
   * MongoDB uses this constructor when deserializing documents from the database.
   */
  public User() {}

  /**
   * Parameterized constructor for creating a new User with username and password.
   * This is typically used during user registration.
   * The password will be encoded before being stored in the database.
   *
   * @param username The username for the new user
   * @param password The raw password that will be encoded before storage
   */
  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * Gets the user's unique identifier.
   *
   * @return The user's ID
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the user's unique identifier.
   * This is typically handled by MongoDB during document insertion.
   *
   * @param id The ID to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the user's username.
   *
   * @return The username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the user's username.
   *
   * @param username The username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the user's hashed password.
   * This method is used by Spring Security during authentication to
   * compare the provided password with the stored hash.
   *
   * @return The hashed password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the user's password.
   * When called from the registration process, this would typically
   * receive an already-hashed password from the passwordEncoder.
   *
   * @param password The password to set (typically already hashed)
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Creates a string representation of the User object.
   * Intentionally excludes the password field for security reasons.
   *
   * @return A string representation of the User object
   */
  @Override
  public String toString() {
    return (
      "User{" + "id='" + id + '\'' + ", username='" + username + '\'' + '}'
    );
  }
}
