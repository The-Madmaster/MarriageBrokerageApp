# Start with a base image that includes Java
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your build (assuming 'mvn clean package' has been run)
# The JAR name should match the one generated in your 'target' directory
COPY target/marriage-bureau-software-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Command to run your application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]