package tourGuide.controller;

import common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.controller.TourGuideController;
import tourGuide.service.TourGuideService;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TourGuideController.class)
@ExtendWith(SpringExtension.class)
public class TourGuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TourGuideService tourGuideService;

    @Test
    void indexTest() throws Exception {
        //Act
        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isOk()).andExpect(content().string(containsString(
                        "Greetings from TourGuide!")));
    }

    @Test
    void getLocationTest() throws Exception {
        Location location = new Location(33.817595D,-117.922008D);
        Date date = new Date();
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, date);
        Mockito.when(tourGuideService.getUserLocation(visitedLocation.getUserId())).thenReturn(visitedLocation);

        mockMvc.perform(get("/getLocation").param("userId", String.valueOf(visitedLocation.getUserId())))
                .andExpect(status().isOk());
    }

    @Test
    void getNearbyAttractionsTest() throws Exception {
        String userName = "username";
        String phoneNo = "000";
        String email = "@gmail.com";
        User user = new User(UUID.randomUUID(), userName,phoneNo, email);
        Mockito.when(tourGuideService.getUser(user.getUserName())).thenReturn(user);

        mockMvc.perform(get("/getNearByAttractions").param("userName", userName))
                .andExpect(status().isOk());
    }

    @Test
    void getRewardsTest() throws Exception {
        //Act
        mockMvc.perform(get("/getRewards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userName", "internalUser1"))
                //Assert
                .andExpect(status().isOk());

    }

    @Test
    void getAllCurrentLocationsTest() throws Exception {
        Mockito.when(tourGuideService.getAllUsers()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/getAllCurrentLocations"))
                .andExpect(status().isOk());
    }

    @Test
    void getTripDealsTest() throws Exception {
        String userName = "username";
        Mockito.when(tourGuideService.getTripDeals(tourGuideService.getUser(userName))).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/getTripDeals").param("userName", userName))
                .andExpect(status().isOk());
    }
}
