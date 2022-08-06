# Mirror using openjdk11
FROM openjdk:11

# Copy the jar package into the container
ADD build/libs/RewardCentral.jar reward.jar

# Expose port 8082
EXPOSE 8082

# Run the jar packagereward
#CMD java -jar rewardCentral.jar

ENTRYPOINT ["java", "-jar", "reward.jar"]