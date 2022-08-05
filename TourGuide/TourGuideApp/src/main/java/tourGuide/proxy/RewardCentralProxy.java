package tourGuide.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

//@FeignClient(value="microservice-rewardCentral", url="${tourguide.microservice-rewardsCentral}")
@FeignClient(value="microservice-rewardCentral", url="localhost:8083")
public interface RewardCentralProxy {

    @GetMapping("/rewardPoints")
    int getAttractionRewardPoints(@RequestParam("attractionId") UUID attractionId, @RequestParam("userId") UUID userId);
}
