package dev.leighton.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the main entry point for the Movies application.
 * It serves as the starting point of the Spring Boot application and initializes the Spring context.
 */
@SpringBootApplication
/**
 * The @SpringBootApplication annotation is a convenience annotation that combines:
 * 1. @Configuration: Tags the class as a source of bean definitions for the application context.
 * 2. @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings,
 *    other beans, and various property settings.
 * 3. @ComponentScan: Tells Spring to look for other components, configurations, and services in the
 *    specified package (dev.leighton.movies) and its sub-packages.
 */
public class MoviesApplication {

  /**
   * The main method serves as the entry point for the application.
   * When executed, it launches the Spring application context, making the application operational.
   *
   * @param args Command line arguments passed to the application. These can be used to customize
   *             Spring Boot startup behavior or to pass arguments to the application itself.
   */
  public static void main(String[] args) {
    /**
     * SpringApplication.run() method does the following:
     * 1. Creates an appropriate ApplicationContext instance based on the classpath
     * 2. Registers any CommandLinePropertySource to expose command line arguments as Spring properties
     * 3. Refreshes the application context, loading all singleton beans
     * 4. Triggers any embedded web server (e.g., Tomcat) to start
     * 5. Sets up the entire Spring ecosystem, including auto-configuration and component scanning
     *
     * MoviesApplication.class is passed as the primary configuration class
     * args is passed to allow command-line arguments to be used by Spring Boot
     */
    SpringApplication.run(MoviesApplication.class, args);
  }
}
