package dev.leighton.movies;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "dev.leighton.movies")
public class MongoConfig {

  @Value("${spring.data.mongodb.host}")
  private String host;

  @Value("${spring.data.mongodb.username}")
  private String username;

  @Value("${spring.data.mongodb.password}")
  private String password;

  @Value("${spring.data.mongodb.database}")
  private String database;

  @Value("${spring.data.mongodb.authentication-database}")
  private String authDatabase;

  @Bean
  public MongoClient mongoClient() {
    MongoCredential credential = MongoCredential.createCredential(
      username,
      authDatabase,
      password.toCharArray()
    );

    ConnectionString connectionString = new ConnectionString(
      "mongodb+srv://" + host + "/" + database + "?retryWrites=true&w=majority"
    );

    MongoClientSettings settings = MongoClientSettings.builder()
      .applyConnectionString(connectionString)
      .credential(credential)
      .build();

    return MongoClients.create(settings);
  }

  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongoClient(), database);
  }
}
