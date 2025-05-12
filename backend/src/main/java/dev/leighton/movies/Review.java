package dev.leighton.movies;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

  private ObjectId id;

  private String body;

  private LocalDateTime created;

  private LocalDateTime updated;

  public Review(String body, LocalDateTime created, LocalDateTime updated) {
    this.body = body;
    this.created = created;
    this.updated = updated;
  }
}
