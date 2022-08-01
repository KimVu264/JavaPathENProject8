package tourGuide.service;

import common.dto.NearByAttractionsDto;
import common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.helper.InternalTestingData;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardCentralProxy;
import tourGuide.proxy.TripPricerProxy;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class TourGuideService {

    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

    public final UserService userService;
    private final GpsUtilProxy gpsUtilProxy;
    private final RewardsService rewardsService;
    private final RewardCentralProxy rewardCentralProxy;
    private final TripPricerProxy tripPricerProxy;
    public InternalTestingData internalTestingData;
    public final TrackerService trackerService;

    public ExecutorService service = Executors.newFixedThreadPool(100);

    public TourGuideService(UserService userService, GpsUtilProxy gpsUtilProxy, RewardCentralProxy rewardCentralProxy, TripPricerProxy tripPricerProxy, InternalTestingData internalTestingData) {
        this.userService = userService;
        this.gpsUtilProxy = gpsUtilProxy;
        this.rewardCentralProxy = rewardCentralProxy;
        this.tripPricerProxy = tripPricerProxy;
        this.internalTestingData = internalTestingData;

        //logger.info("TestMode enabled");
        //logger.debug("Initializing users", userService.getAllUsers().size());
        internalTestingData.initializeInternalUsers();
        logger.debug("Finished initializing users");

        trackerService = new TrackerService(this);
        rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        addShutDownHook();
    }

    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public VisitedLocation getUserLocation(UUID userId) {
        //logger.info(" Search for user visited location");
        return gpsUtilProxy.getUserLocation(userId);
    }

    public User getUser(String userName) {
        return internalTestingData.internalUserMap.get(userName);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(internalTestingData.internalUserMap.values());
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
        logger.debug("track user location");
        return CompletableFuture.runAsync(() -> gpsUtilProxy.getUserLocation(userId), service);
    }

    public List<NearByAttractionsDto> getNearByAttractions(UUID userId) {
        List<NearByAttractionsDto> nearbyAttractionsListDto = new ArrayList<>();
        VisitedLocation userLocation = getUserLocation(userId);
        for (Attraction attraction : gpsUtilProxy.getAttractions()) {
            if (rewardsService.isWithinAttractionProximity(attraction, userLocation.getLocation())) {
                NearByAttractionsDto nearBy = new NearByAttractionsDto();
                nearBy.setAttraction(attraction);
                nearBy.setUserLocation(userLocation.getLocation());
                nearBy.setDistance(rewardsService.getDistance(new Location(attraction.getLongitude(), attraction.getLatitude()), userLocation.getLocation()));
                nearBy.setRewardPoints(rewardCentralProxy.getAttractionRewardPoints(attraction.getAttractionId(), userId));
                nearbyAttractionsListDto.add(nearBy);
                logger.info("Get nearby attractions: {}", nearBy);
                nearbyAttractionsListDto.add(nearBy);
            }
        }

        return nearbyAttractionsListDto.stream().limit(5).collect(Collectors.toList());
    }

    public Map<String, Location> getAllUsersLocation() {
        logger.debug("getting all users location");
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
