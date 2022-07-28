package tourGuide.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import gpsUtil.location.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.dto.NearByAttractionsDto;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.exception.DataNotFoundException;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

	@Autowired
	TourGuideService tourGuideService;

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
    public String getLocation(@RequestParam String userName) {
		logger.debug("get location request");
		return JsonStream.serialize(tourGuideService.getUserLocation(getUser(userName)));
    }

	/**
	 * Get the closest five tourist attractions to the user
	 * @param userName
	 * @return  a List of Attractions that contains name of tourist attraction, user's location, distance between user and attraction, reward points for each attraction
	 */

    @RequestMapping("/getNearbyAttractions")
    public List<NearByAttractionsDto> getNearbyAttractions(@RequestParam String userName) {
		logger.debug("near by attractions request");
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
	    List<NearByAttractionsDto> attractionList = tourGuideService.getNearByAttractions(visitedLocation);
    	return attractionList;
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
    public String getAllCurrentLocations() {
			logger.info("Get all users current Location");
			return JsonStream.serialize(tourGuideService.getAllUsersLocation());
    }

    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
		logger.info("Get trip deals of user: {}", userName);
    	return providers;
    }

}
