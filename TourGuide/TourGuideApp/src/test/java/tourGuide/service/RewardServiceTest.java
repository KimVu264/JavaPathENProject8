package tourGuide.service;

import common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.proxy.RewardCentralProxy;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

    @Mock
    private GpsUtilService      gpsUtilService;
    @Mock
    private RewardCentralProxy rewardCentralProxy;
    @InjectMocks
    private RewardsService      rewardsService;

    private User user;
    private VisitedLocation visitedLocation;
    private Location location;
    private UserReward userReward;
    private Attraction attraction;

    @BeforeEach
    void setUp() {
        user             = new User(UUID.randomUUID(), "john", "123445", "john@tourguide.com");
        location         = new Location(33.817595D, -117.922008D);
        visitedLocation  = new VisitedLocation(UUID.randomUUID(), location, new Date());
        attraction = new Attraction(33.817595D, -117.922008D, "Disneyland", "Anaheim", "CA", UUID.randomUUID());
        userReward = new UserReward(visitedLocation, attraction, 22);
    }

    @Test
    void calculateRewardsTest() {
        //Arrange
        user.addToVisitedLocations(visitedLocation);
        when(gpsUtilService.getAttractions()).thenReturn(List.of(attraction));
        when(rewardCentralProxy.getAttractionRewardPoints(attraction.getAttractionId(), user.getUserId())).thenReturn(23);
        //Act
        rewardsService.calculateRewards(user);
        //Assert
        verify(gpsUtilService).getAttractions();

    }
}
