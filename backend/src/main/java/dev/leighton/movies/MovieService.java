package dev.leighton.movies;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

  @Autowired
  private MovieRepository repository;

  public List<Movie> findAllMovies() {
    return repository.findAll();
  }

  public Optional<Movie> findMovieByImdbId(String imdbId) {
    // Delegates to the repository's custom query method
    return repository.findMovieByImdbId(imdbId);
  }
}
