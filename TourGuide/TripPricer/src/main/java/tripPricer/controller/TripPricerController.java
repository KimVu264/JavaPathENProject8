package tripPricer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;
import tripPricer.service.TripPricerService;

import java.util.List;
import java.util.UUID;

@RestController
public class TripPricerController {

    @Autowired
    private TripPricerService tripPricerService;

    @GetMapping("tripDeals")
    public List<Provider> getTripDeals (@RequestParam UUID userId, @RequestParam int numberOfAdults, @RequestParam int numberOfChildren, @RequestParam int tripDuration, @RequestParam int rewardPoints ) {
        return tripPricerService.getPricer(userId, numberOfAdults, numberOfChildren, tripDuration, rewardPoints);
    }

}
