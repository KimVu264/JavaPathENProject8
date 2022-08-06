package tourGuide.service;

import common.dto.NearByAttractionsDto;
import common.dto.UserLocationDto;
import common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.helper.InternalTestingData;
import tourGuide.proxy.TripPricerProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service 
public class TourGuideService {

    public final UserService userService;
    public final TrackerService trackerService;
    private final GpsUtilService gpsUtilService;
    private final RewardsService rewardsService;
    private final TripPricerProxy tripPricerProxy;
    public InternalTestingData internalTestingData;
    public ExecutorService service = Executors.newFixedThreadPool(200);
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

    public TourGuideService(RewardsService rewardsService, UserService userService, GpsUtilService gpsUtilService, TripPricerProxy tripPricerProxy, InternalTestingData internalTestingData) {
        this.rewardsService = rewardsService;
        this.userService = userService;
        this.gpsUtilService = gpsUtilService;
        this.tripPricerProxy = tripPricerProxy;
        this.internalTestingData = internalTestingData;

        logger.info("TestMode enabled");
        logger.debug("Initializing users", userService.getAllUsers().size());
        internalTestingData.initializeInternalUsers();
        logger.debug("Finished initializing users");

        trackerService = new TrackerService(this);
        addShutDownHook();
    }

    public List<UserReward> getUserRewards(User user) {
        logger.info("Reward list for user: {}",user);
        return user.getUserRewards();
    }

    public VisitedLocation getUserLocation(UUID userId) {
        return gpsUtilService.getUserLocation(userId);
    }

    public User getUser(String userName) {
        return userService.getUser(userName);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public List<Provider> getTripDeals(User user) {
        logger.debug("getting Trip Deals for userName : {}", user.getUserName());
        List<Provider> providers = tripPricerProxy.getTripDeals(user.getUserId(),
                user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
                user.getUserPreferences().getTripDuration(), user.getUserRewards()
                        .stream()
                        .mapToInt(UserReward::getRewardPoints)
                        .sum());
        user.setTripDeals(providers);
        return providers;
    }

    public CompletableFuture<?> trackUserLocation(UUID userId) {
        Locale.setDefault(Locale.US);
        //logger.debug("track user location");
        return CompletableFuture.runAsync(() -> gpsUtilService.getUserLocation(userId), service);
    }

    public List<NearByAttractionsDto> getNearByAttractions(UUID userId) {
        List<NearByAttractionsDto> nearbyAttractionsListDto = new ArrayList<>();
        VisitedLocation userLocation = getUserLocation(userId);
        for (Attraction attraction : gpsUtilService.getAttractions()) {
            if (rewardsService.isWithinAttractionProximity(attraction, userLocation.getLocation())) {
                NearByAttractionsDto nearBy = new NearByAttractionsDto();
                nearBy.setAttractionName(attraction.getAttractionName());
                nearBy.setDistance(rewardsService.getDistance(new Location(attraction.getLongitude(), attraction.getLatitude()), userLocation.getLocation()));
                nearBy.setRewardPoints(rewardsService.getRewardPoints(attraction.getAttractionId(), userId));
                logger.info("Get nearby attractions: {}", nearBy);
                nearbyAttractionsListDto.add(nearBy);
            }
        }

        return nearbyAttractionsListDto.stream().limit(5).collect(Collectors.toList());
    }

    public List<UserLocationDto> getAllCurrentLocations() {
        logger.info("Get all users current Location");
        return userService.getAllCurrentLocations();
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                trackerService.stopTracking();
            }
        });
    }

}
