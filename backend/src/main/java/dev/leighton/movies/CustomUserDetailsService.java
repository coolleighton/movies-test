package dev.leighton.movies;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

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
    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPassword(),
      new ArrayList<>()
    );
  }
}
