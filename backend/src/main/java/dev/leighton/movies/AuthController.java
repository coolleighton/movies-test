package dev.leighton.movies;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles all authentication-related HTTP endpoints including user registration,
 * login, and retrieving user information.
 * It uses Spring Security for authentication and authorization.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody User user) {
    // Check if username already exists in the database
    if (userRepository.findByUsername(user.getUsername()) != null) {
      // Create error response if username already exists
      Map<String, String> response = new HashMap<>();
      response.put("error", "Username already exists");
      return ResponseEntity.badRequest().body(response); // Returns HTTP 400 Bad Request
    }

    // Encode the user's password before saving it (never store plaintext passwords)
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    // Save the new user to the database
    userRepository.save(user);

    // Create success response
    Map<String, String> response = new HashMap<>();
    response.put("message", "User registered successfully!");
    return ResponseEntity.ok(response); // Returns HTTP 200 OK
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    // Create authentication token with user credentials
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginRequest.getUsername(),
        loginRequest.getPassword()
      )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Create success response
    Map<String, String> response = new HashMap<>();
    response.put("message", "Login successful!");
    return ResponseEntity.ok(response); // Returns HTTP 200 OK
  }

  @GetMapping("/user")
  public ResponseEntity<?> getUserInfo(Principal principal) {
    // Check if user is authenticated
    if (principal == null) {
      // Create error response if not authenticated
      Map<String, String> response = new HashMap<>();
      response.put("error", "Not authenticated");
      return ResponseEntity.status(401).body(response); // Returns HTTP 401 Unauthorized
    }

    // Retrieve user from database using the username from Principal
    User user = userRepository.findByUsername(principal.getName());
    // Check if user exists
    if (user == null) {
      // Create error response if user not found (unlikely in normal operation)
      Map<String, String> response = new HashMap<>();
      response.put("error", "User not found");
      return ResponseEntity.status(404).body(response); // Returns HTTP 404 Not Found
    }

    // Create response with non-sensitive user information
    Map<String, Object> userResponse = new HashMap<>();
    userResponse.put("id", user.getId());
    userResponse.put("username", user.getUsername());

    return ResponseEntity.ok(userResponse); // Returns HTTP 200 OK with user information
  }
}
