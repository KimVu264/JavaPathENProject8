package tourGuide.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.exception.DataNotFoundException;
import tourGuide.model.User;
import tourGuide.service.TourGuideService;

import java.util.List;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);
    @Autowired
    TourGuideService tourGuideService;

    @RequestMapping("/allUsers")
    private List<User> getAllUsers() {
        logger.info("Search list of all users");
        return tourGuideService.getAllUsers();
    }

    @RequestMapping("/userPreferences")
    public ResponseEntity<String> updateUserPreferences(@RequestParam String userName, @RequestBody UserPreferencesDto userPreferences) throws DataNotFoundException {
        logger.debug("Update user preferences of {} request ",userName);
        tourGuideService.updateUserPreferences(userName, userPreferences);
        return ResponseEntity.status(HttpStatus.OK).body("user preferences saved successfully !!");
    }
}
