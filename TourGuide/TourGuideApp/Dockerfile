# Mirror using openjdk11
FROM openjdk:11

# Copy the jar package into the container
ADD build/libs/TourGuideApp.jar tourguide.jar

# Expose port 8080
EXPOSE 8080

# Run the jar package
#CMD java -jar TourGuideApp.jar

ENTRYPOINT ["java", "-jar", "tourguide.jar"]