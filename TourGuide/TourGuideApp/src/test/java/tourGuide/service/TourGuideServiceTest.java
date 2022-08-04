package tourGuide.service;

import common.dto.UserLocationDto;
import common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.helper.InternalTestingData;
import tourGuide.proxy.TripPricerProxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TourGuideServiceTest {

    @MockBean
    private TripPricerProxy tripPricerProxy;
    @MockBean
    private InternalTestingData internalTestingData;
    private TourGuideService       tourGuideService;
    @MockBean
    private RewardsService         rewardsService;
    @MockBean
    private UserService            userService;
    @MockBean
    private GpsUtilService         gpsUtilService;

    private User user;
    private VisitedLocation visitedLocation;
    private Location location;
    private UserReward userReward;
    private Attraction attraction;
    List<UserLocationDto> userLocationList;

    @BeforeEach
    void setUp() {

        userLocationList = new ArrayList<>();
        tourGuideService = new TourGuideService(rewardsService, userService,gpsUtilService,tripPricerProxy,internalTestingData );
        user             = new User(UUID.randomUUID(), "john", "123445", "john@tourguide.com");
        location         = new Location(33.817595D, -117.922008D);
        visitedLocation  = new VisitedLocation(UUID.randomUUID(), location, new Date());
        attraction       = new Attraction(33.817595D, -117.922008D, "Disneyland", "Anaheim", "CA", UUID.randomUUID());
        userReward       = new UserReward(visitedLocation, attraction, 22);
    }


    @Test
    void GetUserLocationTest_withEmptyVisitedLocations_shouldReturnUserLocation() {
        //Arrange
        doReturn(visitedLocation).when(gpsUtilService).getUserLocation(user.getUserId());
        //Act
        VisitedLocation result = tourGuideService.getUserLocation(user.getUserId());
        //Assert
        assertThat(result).isEqualToComparingFieldByField(visitedLocation);

    }

    @Test
    void TrackUserLocationTest(){
        //Arrange
        doReturn(visitedLocation).when(gpsUtilService).getUserLocation(user.getUserId());
        //Act
        VisitedLocation result = tourGuideService.getUserLocation(user.getUserId());
        //Assert
        assertThat(result).isEqualToComparingFieldByField(visitedLocation);

    }


    @Test
    void getAllUsersLocationTest() {
        Mockito.when(userService.getAllCurrentLocations()).thenReturn(userLocationList);
        tourGuideService.getAllCurrentLocations();
        Mockito.verify(userService,Mockito.times(1)).getAllCurrentLocations();
    }

    @Test
    void getAllUsersTest() {
        //Arrange
        when(userService.getAllUsers()).thenReturn(List.of(user));
        //Act
        List<User> users = tourGuideService.getAllUsers();
        //Assert
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void getTripDealsTest() {
        //Arrange
        Provider provider1 = new Provider("Holiday Travels", 34, UUID.randomUUID());
        Provider provider2 = new Provider("Holiday Travels", 34, UUID.randomUUID());
        user.addUserReward(userReward);
        UserPreferences userPreferences = new UserPreferences(4, 3, 2, 1);
        user.setUserPreferences(userPreferences);
        user.addUserReward(userReward);
        when(tripPricerProxy.getTripDeals(user.getUserId(),
                user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(),
                user.getUserPreferences().getTripDuration(),
                user.getUserRewards()
                        .stream()
                        .mapToInt(UserReward::getRewardPoints)
                        .sum())).
                thenReturn(List.of(provider1, provider2));
        //Act
        List<Provider> providers = tourGuideService.getTripDeals(user);
        //Assert
        assertThat(providers.size()).isEqualTo(2);
    }
}
