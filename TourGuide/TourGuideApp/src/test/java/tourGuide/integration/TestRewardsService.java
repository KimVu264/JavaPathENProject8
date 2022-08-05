package tourGuide.integration;

import common.model.Attraction;
import common.model.User;
import common.model.UserReward;
import common.model.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.helper.InternalTestHelper;
import tourGuide.helper.InternalTestingData;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.repository.UserRepository;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestRewardsService {

	@Autowired
	private GpsUtilService gpsUtilService;
	@Autowired
	private UserService userService;
	@Autowired
	private TripPricerProxy tripPricerProxy;
	@Autowired
	private RewardsService rewardsService;
	@Autowired
	private TourGuideService tourGuideService;
	@Autowired
	private InternalTestingData internalTestingData;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void init() {
		Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
		internalTestingData = new InternalTestingData(userRepository);

	}

	@Test
	public void userGetRewards()  throws ExecutionException, InterruptedException{

		InternalTestingData internalTestingData = new InternalTestingData(userRepository);
		InternalTestHelper.setInternalUserNumber(1);
		tourGuideService = new TourGuideService(rewardsService,userService, gpsUtilService, tripPricerProxy, internalTestingData);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

		tourGuideService.trackUserLocation(user.getUserId());
		rewardsService.calculateRewards(user).get();
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.trackerService.stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	//@Ignore // Needs fixed - can throw ConcurrentModificationException

	@Test
	public void nearAllAttractions() throws ExecutionException, InterruptedException  {

		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(rewardsService,userService, gpsUtilService, tripPricerProxy, internalTestingData);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

		rewardsService.calculateRewards(user).get();
		List<UserReward> userRewards = tourGuideService.getUserRewards(user);
		tourGuideService.trackerService.stopTracking();

		assertEquals(gpsUtilService.getAttractions().size(), userRewards.size());
	}
}
