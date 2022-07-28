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
import tourGuide.dto.UserPreferencesDto;
import tourGuide.dto.UserRewardDto;
import tourGuide.exception.DataNotFoundException;
import tourGuide.model.*;
import tourGuide.helper.InternalTestingData;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static tourGuide.helper.InternalTestingData.tripPricerApiKey;

@Service
public class TourGuideService {

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final RewardCentral rewardCentral;
	private final TripPricer tripPricer = new TripPricer();
	public final TrackerService trackerService;
	public InternalTestingData internalTestingData;
	boolean testMode = true;

	public ExecutorService service = Executors.newFixedThreadPool(100);

	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService, RewardCentral rewardCentral, InternalTestingData internalTestingData) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
		this.rewardCentral = rewardCentral;
		this.internalTestingData = internalTestingData;

		if(testMode) {
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

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation;
		visitedLocation = (user.getVisitedLocations().size() > 0) ?
				user.getLastVisitedLocation() :
				trackUserLocation(user);
		return visitedLocation;
	}

	public User getUser(String userName) {
		return internalTestingData.internalUserMap.get(userName);
	}

	public List<User> getAllUsers() {
		return new ArrayList<>(internalTestingData.internalUserMap.values());
	}

	public void addUser(User user) {
		if(!internalTestingData.internalUserMap.containsKey(user.getUserName())) {
			internalTestingData.internalUserMap.put(user.getUserName(), user);
		}
	}

	public void updateUserPreferences(String userName, UserPreferencesDto userPreferences) throws DataNotFoundException {
		logger.debug("get user {}", userName);
		User user = getUser(userName);
		user.setUserPreferences(new UserPreferences(userPreferences.getTripDuration(), userPreferences.getTicketQuantity(), userPreferences.getNumberOfAdults(), userPreferences.getNumberOfChildren()));
	}

	public List<Provider> getTripDeals(User userDto) {
		int cumulRewardPoints = userDto.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();

		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, userDto.getUserId(), userDto.getUserPreferences().getNumberOfAdults(),
				userDto.getUserPreferences().getNumberOfChildren(), userDto.getUserPreferences().getTripDuration(), cumulRewardPoints);
		userDto.setTripDeals(providers);
		return providers;
	}

	public VisitedLocation trackUserLocation(User user) {
		Locale.setDefault(Locale.US);
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	public List<NearByAttractionsDto> getNearByAttractions(VisitedLocation visitedLocation) {
		List<NearByAttractionsDto> nearbyAttractionsListDto = new ArrayList<>();
		for(Attraction attraction : gpsUtil.getAttractions()) {
			if(rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
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
						toMap(u -> u.getUserId().toString(), u -> getUserLocation(u).location));
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
		      public void run() {
		        trackerService.stopTracking();
		      }
		    });
	}

}
