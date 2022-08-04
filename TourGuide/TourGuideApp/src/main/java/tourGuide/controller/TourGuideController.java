package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import common.dto.NearByAttractionsDto;
import common.dto.UserLocationDto;
import common.model.Provider;
import common.model.User;
import common.model.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.service.TourGuideService;

import java.util.List;
import java.util.UUID;

@RestController
public class TourGuideController {

    @Autowired
    TourGuideService tourGuideService;
    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

    @RequestMapping("/")
    public String index() {
        logger.info("get home page");
        return "Greetings from TourGuide!";
    }

    @RequestMapping("/getUser")
    private User getUser(String userName) {
        logger.info("Get user: {}", userName);
        return tourGuideService.getUser(userName);
    }

    @RequestMapping("/getLocation")
    public VisitedLocation getLocation(@RequestParam UUID userId) {
        logger.info("Get user locations with userId: {}", userId);
        return tourGuideService.getUserLocation(userId);
    }

    /**
     * Get the closest five tourist attractions to the user
     *
     * @param userName
     * @return a List of Attractions that contains name of tourist attraction, user's location, distance between user and attraction, reward points for each attraction
     */

    @RequestMapping("/getNearByAttractions")
    public List<NearByAttractionsDto> getNearbyAttractions(@RequestParam String userName) {
        logger.info("Get nearby attractions with username: {}", userName);
        UUID userId = tourGuideService.getUser(userName).getUserId();
        return tourGuideService.getNearByAttractions(userId);
    }

    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        logger.info("Get rewards with user: {}", userName);
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    //  Get a list of every user's most recent location as JSON
    //- Note: does not use gpsUtil to query for their current location,
    //        but rather gathers the user's current location from their stored location history.
    //
    // Return object should be the just a JSON mapping of userId to Locations similar to:
    //     {
    //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
    //        ...
    //     }

    @RequestMapping("/getAllCurrentLocations")
    public List<UserLocationDto> getAllCurrentLocations() {
        logger.info("Get all users current Location");
        return tourGuideService.getAllCurrentLocations();
    }

    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        logger.info("Get trip deals of user: {}", userName);
        return providers;
    }

}
