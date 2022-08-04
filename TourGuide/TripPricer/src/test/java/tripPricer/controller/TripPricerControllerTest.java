package tripPricer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tripPricer.Provider;
import tripPricer.service.TripPricerService;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TripPricerController.class)
public class TripPricerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TripPricerService tripPricerService;

    @Test
    void getTripDealsTest() throws Exception {
        //Arrange
        UUID userId = UUID.randomUUID();
        Provider provider = new Provider(UUID.randomUUID(), "name", 34);

        when(tripPricerService.getPricer(userId, 2, 1, 4, 44)).thenReturn(List.of(provider));
        //Act
        mockMvc.perform(get("/tripDeals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(userId))
                        .param("numberOfAdults", "2")
                        .param("numberOfChildren", "1")
                        .param("tripDuration", "4")
                        .param("rewardPoints", "44")
                )
                //Assert
                .andExpect(status().isOk());
    }
}
