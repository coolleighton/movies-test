package dev.leighton.movies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

  @Autowired
  private MovieService service;

  @GetMapping
  public ResponseEntity<List<Movie>> getMovies(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    // Debug information
    System.out.println("==== Movie Request ====");
    System.out.println("Request URI: " + request.getRequestURI());
    System.out.println("Session ID: " + request.getSession().getId());
    System.out.println("Is new session: " + request.getSession().isNew());

    // Print all headers
    System.out.println("Request Headers:");
    java.util.Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      System.out.println(headerName + ": " + request.getHeader(headerName));
    }

    // Print all cookies
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      System.out.println("Cookies received:");
      for (Cookie cookie : cookies) {
        System.out.println(cookie.getName() + ": " + cookie.getValue());
      }
    } else {
      System.out.println("No cookies received");
    }

    // Call the service layer to retrieve all movies from the database
    List<Movie> allMovies = service.findAllMovies();

    // For troubleshooting, explicitly add the session cookie to every response
    String sessionId = request.getSession().getId();
    response.addHeader(
      "Set-Cookie",
      "JSESSIONID=" + sessionId + "; Path=/; HttpOnly; SameSite=None; Secure"
    );

    // Return the list of movies wrapped in a ResponseEntity with HTTP 200 OK status
    return new ResponseEntity<List<Movie>>(allMovies, HttpStatus.OK);
  }

  @GetMapping("/{imdbId}")
  public ResponseEntity<Optional<Movie>> getSingleMovie(
    @PathVariable String imdbId, // Extract the imdbId value from the URL path
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    // Debug information
    System.out.println("==== Single Movie Request ====");
    System.out.println("Request URI: " + request.getRequestURI());
    System.out.println("Session ID: " + request.getSession().getId());
    System.out.println("Is new session: " + request.getSession().isNew());

    // Print all cookies
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      System.out.println("Cookies received:");
      for (Cookie cookie : cookies) {
        System.out.println(cookie.getName() + ": " + cookie.getValue());
      }
    } else {
      System.out.println("No cookies received");
    }

    // Call the service layer to find a specific movie by its IMDb ID
    Optional<Movie> movie = service.findMovieByImdbId(imdbId);

    // For troubleshooting, explicitly add the session cookie to every response
    String sessionId = request.getSession().getId();
    response.addHeader(
      "Set-Cookie",
      "JSESSIONID=" + sessionId + "; Path=/; HttpOnly; SameSite=None; Secure"
    );

    // Return the movie wrapped in a ResponseEntity with HTTP 200 OK status
    return new ResponseEntity<Optional<Movie>>(movie, HttpStatus.OK);
  }
}
