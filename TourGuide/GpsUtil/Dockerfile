# Mirror using openjdk11
FROM openjdk:11

# Copy the jar package into the container
ADD build/libs/GpsUtil.jar gps.jar

# Expose port 9000
EXPOSE 9000

# Run the jar package
# CMD java -jar gpsUtil.jar

ENTRYPOINT ["java", "-jar", "gps.jar"]