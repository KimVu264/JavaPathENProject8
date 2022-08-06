# Mirror using openjdk11
FROM openjdk:11

# Copy the jar package into the container
ADD build/libs/TripPricer.jar trip.jar

# Expose port 8082
EXPOSE 8083

# Run the jar package
# CMD java -jar tripPricer.jar

ENTRYPOINT ["java", "-jar", "trip.jar"]