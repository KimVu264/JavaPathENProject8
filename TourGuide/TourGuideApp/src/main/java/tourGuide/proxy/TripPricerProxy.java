package tourGuide.proxy;

import common.model.Provider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "microservice-tripPricer", url = "${tourguide.microservice-tripPrice}")
//@FeignClient(name = "microservice-tripPricer", url = "localhost:8083")
public interface TripPricerProxy {

    @GetMapping("tripDeals")
    List<Provider> getTripDeals (@RequestParam("userId") UUID userId, @RequestParam("numberOfAdults") int numberOfAdults, @RequestParam("numberOfChildren") int numberOfChildren, @RequestParam("tripDuration") int tripDuration, @RequestParam("rewardPoints") int rewardPoints );
}
