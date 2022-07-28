package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.dto.NearByAttractionsDto;
import tourGuide.helper.InternalTestHelper;
import tourGuide.helper.InternalTestingData;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tripPricer.Provider;

public class TestTourGuideService {
/*
	@Test
	public void getUserLocation() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestingData internalTestingData = new InternalTestingData();

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, rewardCentral, internalTestingData);

		User userDto = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(userDto);
		tourGuideService.trackerService.stopTracking();
		assertTrue(visitedLocation.userId.equals(userDto.getUserId()));
	}

	@Test
	public void addUser() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestingData internalTestingData = new InternalTestingData();

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, rewardCentral, internalTestingData);

		User userDto = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User userDto2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(userDto);
		tourGuideService.addUser(userDto2);

		User retrivedUserDto = tourGuideService.getUser(userDto.getUserName());
		User retrivedUserDto2 = tourGuideService.getUser(userDto2.getUserName());

		tourGuideService.trackerService.stopTracking();

		assertEquals(userDto, retrivedUserDto);
		assertEquals(userDto2, retrivedUserDto2);
	}

	@Test
	public void getAllUsers() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestingData internalTestingData = new InternalTestingData();

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, rewardCentral, internalTestingData);

		User userDto = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User userDto2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(userDto);
		tourGuideService.addUser(userDto2);

		List<User> allUserDtos = tourGuideService.getAllUsers();

		tourGuideService.trackerService.stopTracking();

		assertTrue(allUserDtos.contains(userDto));
		assertTrue(allUserDtos.contains(userDto2));
	}

	@Test
	public void trackUser() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestingData internalTestingData = new InternalTestingData();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, rewardCentral, internalTestingData);

		User userDto = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(userDto);

		tourGuideService.trackerService.stopTracking();

		assertEquals(userDto.getUserId(), visitedLocation.userId);
	}

	@Ignore // Not yet implemented
	@Test
	public void getNearbyAttractions() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestingData internalTestingData = new InternalTestingData();

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, rewardCentral, internalTestingData);

		User userDto = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(userDto);

		List<NearByAttractionsDto> attractions = tourGuideService.getNearByAttractions(visitedLocation);

		tourGuideService.trackerService.stopTracking();

		assertEquals(5, attractions.size());
	}

	@Test
	public void getTripDeals() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestingData internalTestingData = new InternalTestingData();

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, rewardCentral, internalTestingData );

		User userDto = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(userDto);

		tourGuideService.trackerService.stopTracking();

		assertEquals(10, providers.size());
	}

 */


}
