# Mirror using openjdk11
FROM openjdk:11

# Copy the jar package into the container
ADD build/libs/GpsUtil.jar gps.jar

# Expose port 8081
EXPOSE 8081

# Run the jar package
# CMD java -jar gpsUtil.jar

ENTRYPOINT ["java", "-jar", "gps.jar"]