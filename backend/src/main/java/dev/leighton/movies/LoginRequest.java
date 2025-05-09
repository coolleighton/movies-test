package dev.leighton.movies;

/**
 * This class represents the data structure for a login request.
 * It serves as a Data Transfer Object (DTO) that captures username and password
 * information sent by clients during authentication attempts.
 */
public class LoginRequest {

  /**
   * The username field stores the user identifier provided in the login request.
   * This will be used to look up the user in the database.
   */
  private String username;

  /**
   * The password field stores the plaintext password provided in the login request.
   * This will be compared with the stored hashed password during authentication.
   */
  private String password;

  /**
   * Default no-argument constructor required by Jackson for JSON deserialization.
   * Spring uses this to instantiate the object when parsing JSON from HTTP requests.
   * Without this constructor, Spring would fail to deserialize JSON into this object.
   */
  public LoginRequest() {}

  /**
   * Parameterized constructor for creating a LoginRequest with both fields.
   * This is useful in code when you need to create a LoginRequest instance programmatically.
   *
   * @param username The username for authentication
   * @param password The password for authentication
   */
  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * Getter method for the username.
   * Jackson uses this method when deserializing JSON to map the "username" JSON property
   * to this field, and when serializing this object to JSON.
   *
   * @return The username provided in the login request
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setter method for the username.
   * Jackson uses this method when deserializing JSON to set the value from
   * the "username" JSON property.
   *
   * @param username The username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getter method for the password.
   * Jackson uses this method when deserializing JSON to map the "password" JSON property
   * to this field, and when serializing this object to JSON.
   *
   * @return The password provided in the login request
   */
  public String getPassword() {
    return password;
  }

  /**
   * Setter method for the password.
   * Jackson uses this method when deserializing JSON to set the value from
   * the "password" JSON property.
   *
   * @param password The password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }
}
