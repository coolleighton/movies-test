package dev.leighton.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public String register(@RequestBody User user) {
    if (userRepository.findByUsername(user.getUsername()) != null) {
      return "Username already exists.";
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return "User registered successfully!";
  }

  @PostMapping("/login")
  public String login() {
    return "Login successful!";
    // Actual login handled by Spring Security automatically
  }
}
