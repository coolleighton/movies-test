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

/**
 * Configuration class for MongoDB connection setup.
 * This class handles the configuration of MongoDB client, credentials, and templates
 * needed for the application to connect to MongoDB database.
 */
@Configuration // Marks this class as a Spring configuration class that defines beans
@EnableMongoRepositories(basePackages = "dev.leighton.movies") // Enables MongoDB repositories in the specified package
public class MongoConfig {

  /**
   * MongoDB server host address.
   * Injected from application properties file using Spring's @Value annotation.
   */
  @Value("${spring.data.mongodb.host}")
  private String host;

  /**
   * MongoDB username for authentication.
   * Injected from application properties file.
   */
  @Value("${spring.data.mongodb.username}")
  private String username;

  /**
   * MongoDB password for authentication.
   * Injected from application properties file.
   */
  @Value("${spring.data.mongodb.password}")
  private String password;

  /**
   * Name of the MongoDB database to connect to.
   * Injected from application properties file.
   */
  @Value("${spring.data.mongodb.database}")
  private String database;

  /**
   * Database used for authentication (usually 'admin' in MongoDB).
   * Injected from application properties file.
   */
  @Value("${spring.data.mongodb.authentication-database}")
  private String authDatabase;

  /**
   * Creates and configures a MongoDB client instance.
   * This bean handles the connection to the MongoDB server with the provided credentials.
   *
   * @return MongoClient instance configured with connection settings and credentials
   */
  @Bean
  public MongoClient mongoClient() {
    // Create MongoDB credentials for authentication
    MongoCredential credential = MongoCredential.createCredential(
      username, // Username for authentication
      authDatabase, // Database where user credentials are stored
      password.toCharArray() // Password converted to char array for security
    );

    // Build the MongoDB connection string
    // Using MongoDB Atlas URI format with srv protocol
    ConnectionString connectionString = new ConnectionString(
      "mongodb+srv://" + host + "/" + database + "?retryWrites=true&w=majority"
    );
    // Note: retryWrites=true ensures write operations are retried if they fail
    // w=majority ensures write operations are acknowledged by a majority of replica set members

    // Build the MongoDB client settings with both connection string and credentials
    MongoClientSettings settings = MongoClientSettings.builder()
      .applyConnectionString(connectionString) // Apply the connection string
      .credential(credential) // Apply authentication credentials
      .build();

    // Create and return the MongoDB client with the configured settings
    return MongoClients.create(settings);
  }

  /**
   * Creates a MongoTemplate bean for MongoDB operations.
   * MongoTemplate is a helper class that provides a rich set of MongoDB operations
   * without requiring you to deal with the low-level MongoDB driver API.
   *
   * @return MongoTemplate instance configured with the MongoDB client and database
   */
  @Bean
  public MongoTemplate mongoTemplate() {
    // Create and return a new MongoTemplate with the configured client and database
    return new MongoTemplate(mongoClient(), database);
    // This template will be used by Spring Data MongoDB for all database operations
  }
}
