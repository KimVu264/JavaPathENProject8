package rewardCentral.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import rewardCentral.service.RewardCentralService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardCentralController.class)
public class RewardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RewardCentralService rewardsService;

    @Test
    void getRewardPointsTest () throws Exception {
        Mockito.when(rewardsService.getRewardPoints(any(),any())).thenReturn(24);
        mockMvc.perform(get("/rewardPoints")
                        .param("userId",String.valueOf(UUID.randomUUID()))
                        .param("attractionId",String.valueOf(UUID.randomUUID())))
                .andExpect(status().isOk());
    }
}
