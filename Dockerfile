# Base image with Java 17
FROM eclipse-temurin:17-jdk-alpine

# Working directory inside the container
WORKDIR /app

# Copy compiled JAR file from host into the image
COPY target/*.jar app.jar

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
