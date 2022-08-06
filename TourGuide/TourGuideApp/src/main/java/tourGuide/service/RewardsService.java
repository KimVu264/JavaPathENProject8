package tourGuide.service;

import common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.proxy.RewardCentralProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RewardsService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    @Autowired
    private GpsUtilService gpsUtilService;

    @Autowired
    private RewardCentralProxy rewardCentralProxy;

    public ExecutorService service = Executors.newFixedThreadPool(200);
    private Logger logger = LoggerFactory.getLogger(RewardsService.class);

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;

    public RewardsService(GpsUtilService gpsUtilService, RewardCentralProxy rewardCentralProxy) {
        this.gpsUtilService = gpsUtilService;
        this.rewardCentralProxy = rewardCentralProxy ;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer(int proximityBuffer) {
        this.defaultProximityBuffer =  proximityBuffer;
    }

/*
    public CompletableFuture<List<UserReward>> calculateRewards(User user) {

        //CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
        // CopyOnWriteArrayList<Attraction> attractions = new CopyOnWriteArrayList<>();
        // userLocations.addAll(user.getVisitedLocations());
        //attractions.addAll(gpsUtil.getAttractions());
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = gpsUtilService.getAttractions();
        List<UserReward> userRewards = user.getUserRewards();
        return CompletableFuture.supplyAsync(() -> {
            userLocations.forEach(
                    visitedLocation -> {
                        //List<UserReward> userRewards = new ArrayList<>();
                        attractions.stream().filter(a -> nearAttraction(visitedLocation.location, a)).forEach(a -> {

                                    if (user.getUserRewards().stream().noneMatch(r -> r.getAttraction().getAttractionName().equals(a.getAttractionName()))) {
                                        //user.getUserRewards().add(new UserRewardDto(visitedLocation, a, getRewardPoints(a, user)));
                                        userRewards.add(new UserReward(visitedLocation, a, getRewardPoints(a.getAttractionId(), user.getUserId())));
                                    }
                                });
                                user.getUserRewards().addAll(userRewards);
                    });
            return user.getUserRewards();
        }, service);
    }

 */

    public CompletableFuture<List<UserReward>> calculateRewards(User user) {

        List<Attraction> attractions = gpsUtilService.getAttractions();
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<VisitedLocation> locations = new CopyOnWriteArrayList<>(userLocations);

        return CompletableFuture.supplyAsync(() -> {
            locations.forEach(visitedLocation ->
                    attractions
                            .forEach(attraction -> {
                                if (user.getUserRewards()
                                        .stream()
                                        .noneMatch(r -> r.attraction
                                                .getAttractionName()
                                                .equals(attraction.getAttractionName()))
                                        && nearAttraction(visitedLocation.location, attraction)) {
                                    UserReward userReward = new UserReward(visitedLocation,
                                            attraction, getRewardPoints(attraction.getAttractionId(), user.getUserId()));

                                    user.getUserRewards().add(userReward);
                                }
                            }));
            return user.getUserRewards();
        }, service);
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return !(getDistance(new Location(attraction.getLongitude(),attraction.getLatitude()), location) > attractionProximityRange);
    }

    public boolean nearAttraction(Location location, Attraction attraction) {
        return !(getDistance(attraction, location) > proximityBuffer);
    }

    public int getRewardPoints(UUID attractionId, UUID userId) {
        return rewardCentralProxy.getAttractionRewardPoints(attractionId, userId);
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lon1 = Math.toRadians(loc1.getLongitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double lon2 = Math.toRadians(loc2.getLongitude());

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }

}
