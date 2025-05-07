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
    if (userRepository.findByUsername(user.getUsername()) != null) {
      Map<String, String> response = new HashMap<>();
      response.put("error", "Username already exists");
      return ResponseEntity.badRequest().body(response);
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);

    Map<String, String> response = new HashMap<>();
    response.put("message", "User registered successfully!");
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginRequest.getUsername(),
        loginRequest.getPassword()
      )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    Map<String, String> response = new HashMap<>();
    response.put("message", "Login successful!");
    return ResponseEntity.ok(response);
  }

  @GetMapping("/user")
  public ResponseEntity<?> getUserInfo(Principal principal) {
    if (principal == null) {
      Map<String, String> response = new HashMap<>();
      response.put("error", "Not authenticated");
      return ResponseEntity.status(401).body(response);
    }

    User user = userRepository.findByUsername(principal.getName());
    if (user == null) {
      Map<String, String> response = new HashMap<>();
      response.put("error", "User not found");
      return ResponseEntity.status(404).body(response);
    }

    // Create a response without sensitive information
    Map<String, Object> userResponse = new HashMap<>();
    userResponse.put("id", user.getId());
    userResponse.put("username", user.getUsername());

    return ResponseEntity.ok(userResponse);
  }
}
