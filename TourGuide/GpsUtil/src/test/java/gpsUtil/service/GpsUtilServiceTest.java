package gpsUtil.service;

import common.model.Location;
import common.model.User;
import common.model.VisitedLocation;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class GpsUtilServiceTest {
    @MockBean
    GpsUtil gpsUtil;

    Date LocTimestamp = new Date(System.currentTimeMillis());
    User user = new User(UUID.randomUUID(), "BobLazar", "000", "BobLazar@tourGuide.com");
    Location location = new Location(-117.922008D,33.817595D);
    VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(),location, LocTimestamp);
    List<Attraction> attractionList = new ArrayList<>();

    @Test
    void getUserLocationTest(){
        user.addToVisitedLocations(visitedLocation);
        gpsUtil.getUserLocation(user.getUserId());
        verify(gpsUtil, Mockito.times(1)).getUserLocation(user.getUserId());
    }

    @Test
    void getAttractionTest(){
        attractionList.add(new Attraction("Lala land","City","State",location.getLatitude(), location.getLongitude()));
        List<Attraction> result = gpsUtil.getAttractions();
        verify(gpsUtil, Mockito.times(1)).getAttractions();
        assertNotNull(result);
    }
}
