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
@RestController // Marks this class as a REST controller that handles HTTP requests
@RequestMapping("/api/auth") // Base URL path for all endpoints in this controller
public class AuthController {

  /**
   * Repository interface for accessing and manipulating User entities in the database.
   * Autowired to inject the implementation provided by Spring Boot.
   */
  @Autowired
  private UserRepository userRepository;

  /**
   * Encoder for hashing passwords before storing them.
   * Autowired to inject the implementation configured in Spring Security.
   */
  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Manager that processes authentication requests.
   * Autowired to inject the implementation configured in Spring Security.
   */
  @Autowired
  private AuthenticationManager authenticationManager;

  /**
   * Endpoint for user registration.
   *
   * @param user The user details including username and password sent in the request body
   * @return ResponseEntity with status 200 OK if registration is successful, or 400 Bad Request if username already exists
   */
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

  /**
   * Endpoint for user login.
   *
   * @param loginRequest Object containing username and password from request body
   * @return ResponseEntity with status 200 OK if login is successful
   * @throws BadCredentialsException implicitly if authentication fails (handled by Spring Security)
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    // Create authentication token with user credentials
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginRequest.getUsername(),
        loginRequest.getPassword()
      )
    );
    // If authentication fails, Spring Security will throw an exception and return 401 Unauthorized

    // Store the authenticated user in the security context
    // This allows other endpoints to access the authenticated user
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Create success response
    Map<String, String> response = new HashMap<>();
    response.put("message", "Login successful!");
    return ResponseEntity.ok(response); // Returns HTTP 200 OK
  }

  /**
   * Endpoint for retrieving authenticated user information.
   *
   * @param principal Automatically injected by Spring Security, contains the authenticated user's details
   * @return ResponseEntity with user information if authenticated, or error response if not
   */
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
    // Note: We intentionally exclude password and other sensitive fields
    Map<String, Object> userResponse = new HashMap<>();
    userResponse.put("id", user.getId());
    userResponse.put("username", user.getUsername());

    return ResponseEntity.ok(userResponse); // Returns HTTP 200 OK with user information
  }
}
