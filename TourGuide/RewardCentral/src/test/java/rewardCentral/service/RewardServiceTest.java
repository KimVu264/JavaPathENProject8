package rewardCentral.service;

import common.model.Attraction;
import common.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import rewardCentral.RewardCentral;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class RewardServiceTest {

    @MockBean
    RewardCentral rewardCentral;

    User user = new User(UUID.randomUUID(), "BobLazar", "000656", "kim@gmail.com");
    Attraction attraction = new Attraction("Lala land","City","State",UUID.randomUUID());

    @Test
    void getRewardPoints(){
        rewardCentral.getAttractionRewardPoints(attraction.getAttractionId(), user.getUserId());
        verify(rewardCentral, Mockito.times(1)).getAttractionRewardPoints(attraction.getAttractionId(), user.getUserId());
    }
}
