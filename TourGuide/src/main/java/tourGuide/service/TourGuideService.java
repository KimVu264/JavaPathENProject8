package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.dto.NearByAttractionsDto;
import tourGuide.helper.InternalTestingData;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static tourGuide.helper.InternalTestingData.tripPricerApiKey;

@Service
public class TourGuideService {

    public final TrackerService trackerService;
    private final GpsUtil gpsUtil;
    private final RewardsService rewardsService;
    private final RewardCentral rewardCentral;
    private final TripPricer tripPricer = new TripPricer();
    public InternalTestingData internalTestingData;
    public ExecutorService service = Executors.newFixedThreadPool(100);
    boolean testMode = true;
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

    public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService, RewardCentral rewardCentral, InternalTestingData internalTestingData) {
        this.gpsUtil = gpsUtil;
        this.rewardsService = rewardsService;
        this.rewardCentral = rewardCentral;
        this.internalTestingData = internalTestingData;

        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            internalTestingData.initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        trackerService = new TrackerService(this);
        addShutDownHook();
    }

    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public VisitedLocation getUserLocation(UUID userId) {
        logger.info(" Search for user visited location");
        return gpsUtil.getUserLocation(userId);
    }

    public User getUser(String userName) {
        return internalTestingData.internalUserMap.get(userName);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(internalTestingData.internalUserMap.values());
    }

    public List<Provider> getTripDeals(User userDto) {
        int cumulRewardPoints = userDto.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();

        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, userDto.getUserId(), userDto.getUserPreferences().getNumberOfAdults(),
                userDto.getUserPreferences().getNumberOfChildren(), userDto.getUserPreferences().getTripDuration(), cumulRewardPoints);
        userDto.setTripDeals(providers);
        return providers;
    }

    public CompletableFuture<?> trackUserLocation(UUID userId) {
        Locale.setDefault(Locale.US);
        return CompletableFuture.runAsync(() -> gpsUtil.getUserLocation(userId), service);
    }

    public List<NearByAttractionsDto> getNearByAttractions(VisitedLocation visitedLocation) {
        List<NearByAttractionsDto> nearbyAttractionsListDto = new ArrayList<>();
        for (Attraction attraction : gpsUtil.getAttractions()) {
            if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
                NearByAttractionsDto nearByAtt = new NearByAttractionsDto();
                nearByAtt.setAttraction(attraction);
                nearByAtt.setUserLocation(visitedLocation.location);
                nearByAtt.setDistance(rewardsService.getDistance(attraction, visitedLocation.location));
                nearByAtt.setRewardPoints(rewardCentral.getAttractionRewardPoints(attraction.attractionId, visitedLocation.userId));
                nearbyAttractionsListDto.add(nearByAtt);
            }
        }
        return nearbyAttractionsListDto.stream().limit(5).collect(Collectors.toList());
    }

    public Map<String, Location> getAllUsersLocation() {
        logger.debug("getting get all users location");
        return getAllUsers().stream()
                .collect(Collectors.
                        toMap(u -> u.getUserId().toString(), u -> getUserLocation(u.getUserId()).location));
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                trackerService.stopTracking();
            }
        });
    }

}
