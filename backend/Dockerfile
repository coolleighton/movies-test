# Stage 1: Build Java app
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with proper SSL certs
FROM eclipse-temurin:17-jre

# Install CA certs so Java can validate MongoDB TLS
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
# Add the TLS protocol option to the java command
CMD ["java", "-Dhttps.protocols=TLSv1.2", "-jar", "app.jar"]