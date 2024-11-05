FROM openjdk:17
LABEL maintainer="Pratik Chavan"
ADD target/*.jar instagram-backend-api.jar

# Expose the port your app runs on
EXPOSE 5454

# Command to run the application
ENTRYPOINT ["java", "-jar", "instagram-backend-api.jar"]
