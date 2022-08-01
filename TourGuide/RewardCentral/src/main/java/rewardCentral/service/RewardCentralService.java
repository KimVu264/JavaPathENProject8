package rewardCentral.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Service
public class RewardCentralService {

    private final Logger logger = LoggerFactory.getLogger(RewardCentralService.class);
    private final RewardCentral rewardsCentral;

    @Autowired
    public RewardCentralService() {this.rewardsCentral = new RewardCentral();}

    public int getRewardPoints(UUID attractionId, UUID userId) {

        logger.debug("get Reward Points");
        return rewardsCentral.getAttractionRewardPoints(attractionId, userId);
    }
}
