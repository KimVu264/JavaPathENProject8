package tourGuide.service;

import common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.proxy.GpsUtilProxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GpsUtilServiceTest {

    @Mock
    private GpsUtilProxy gpsUtilProxy;

    @InjectMocks
    private GpsUtilService gpsUtilService;

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
    void getAttractionsTest() {
        //Arrange
        when(gpsUtilProxy.getAttractions()).thenReturn(List.of(attraction, attraction));
        //Act
        List<Attraction> result = gpsUtilService.getAttractions();
        //Assert
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void getAttractionsTest_withEmptyList() {
        //Arrange
        when(gpsUtilProxy.getAttractions()).thenReturn(new ArrayList<Attraction>());
        //Act
        List<Attraction> result = gpsUtilService.getAttractions();
        //Assert
        assertThat(result.size()).isZero();
    }

    @Test
    void getUserLocationTest() {
        //Arrange
        UUID userId = UUID.randomUUID();
        when(gpsUtilProxy.getUserLocation(userId)).thenReturn(visitedLocation);
        //Act
        VisitedLocation result = gpsUtilService.getUserLocation(userId);
        //ASSERT
        assertThat(result).isEqualToComparingFieldByField(visitedLocation);

    }
}
