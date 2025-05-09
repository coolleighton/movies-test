package dev.leighton.movies;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This service class implements UserDetailsService interface from Spring Security
 * to provide custom user authentication.
 * It serves as a bridge between our application's User model and Spring Security's
 * authentication system.
 */
@Service // Marks this class as a Spring Service component to be automatically detected and registered
public class CustomUserDetailsService implements UserDetailsService {

  /**
   * Repository interface for accessing User entities in the database.
   * Autowired to inject the implementation provided by Spring Boot.
   */
  @Autowired
  private UserRepository userRepository;

  /**
   * This method is required by the UserDetailsService interface.
   * It loads a user from the database by username and converts it to Spring Security's UserDetails object.
   *
   * @param username The username of the user to load
   * @return UserDetails object containing authentication information
   * @throws UsernameNotFoundException If no user with the given username exists
   */
  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    // Attempt to find the user in the database by username
    User user = userRepository.findByUsername(username);

    // If user is not found, throw an exception that will be handled by Spring Security
    if (user == null) {
      throw new UsernameNotFoundException(
        "User not found with username: " + username
      );
    }

    // Convert our application's User entity to Spring Security's UserDetails object
    // This is what Spring Security will use for authentication and authorization
    return new org.springframework.security.core.userdetails.User(
      user.getUsername(), // Username for authentication
      user.getPassword(), // Encoded password for authentication
      new ArrayList<>() // Empty list of authorities/roles (no specific roles in this implementation)
      // In a more complex application, you would add granted authorities here
    );
  }
}
