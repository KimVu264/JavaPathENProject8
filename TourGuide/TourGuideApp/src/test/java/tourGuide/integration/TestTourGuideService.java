package tourGuide.integration;

import common.dto.NearByAttractionsDto;
import common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tourGuide.helper.InternalTestHelper;
import tourGuide.helper.InternalTestingData;
import tourGuide.proxy.RewardCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.repository.GpsRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestTourGuideService {

    @Autowired
    private GpsUtilService gpsUtilService;
    @Autowired
    private UserService userService;
    @Autowired
    private TripPricerProxy tripPricerProxy;
    @Autowired
    private TourGuideService tourGuideService;
    @Autowired
    private InternalTestingData internalTestingData;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RewardsService rewardsService;

    @BeforeEach
    public void init() {
        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
        internalTestingData = new InternalTestingData(userRepository);
    }

    @Test
    public void getUserLocation() {

        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(rewardsService,userService, gpsUtilService, tripPricerProxy, internalTestingData);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(user.getUserId());
        tourGuideService.trackerService.stopTracking();
        assertTrue(visitedLocation.userId.equals(user.getUserId()));
    }

    @Test
    public void getAllUsers() {

        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(rewardsService,userService, gpsUtilService, tripPricerProxy, internalTestingData);

        List<User> allUsers = tourGuideService.getAllUsers();

        tourGuideService.trackerService.stopTracking();

        assertThat(allUsers.size()).isGreaterThan(0);
    }

    @Test
    public void trackUser() {
        //Arrange
        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(rewardsService,userService, gpsUtilService, tripPricerProxy, internalTestingData);

        User user = tourGuideService.getAllUsers().get(0);
        //Act

        VisitedLocation visitedLocation = tourGuideService.getUserLocation(user.getUserId());
        tourGuideService.trackerService.stopTracking();

        assertEquals(user.getUserId(), visitedLocation.userId);
    }

    //@Ignore // Not yet implemented
    @Test
    public void getNearbyAttractions() {

        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(rewardsService,userService, gpsUtilService, tripPricerProxy, internalTestingData);
        User user = tourGuideService.getAllUsers().get(0);
        Attraction attraction = gpsUtilService.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(attraction.getLongitude(), attraction.getLatitude()), new Date()));

        List<NearByAttractionsDto> attractionsList = tourGuideService.getNearByAttractions(user.getUserId());
        tourGuideService.trackerService.stopTracking();

        assertEquals(5, attractionsList.size());
    }

    @Test
    public void getTripDeals() {
        //Arrange

        tourGuideService = new TourGuideService(rewardsService,userService, gpsUtilService, tripPricerProxy, internalTestingData);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        //Assert
        List<Provider> providers = tourGuideService.getTripDeals(user);

        tourGuideService.trackerService.stopTracking();
        //Assert
        assertEquals(5, providers.size());
    }

}
