package tourGuide.controller;

import common.dto.UserDto;
import common.dto.UserPreferencesDto;
import common.model.Provider;
import common.model.User;
import common.model.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tourGuide.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {this.userService = userService;}

    @GetMapping("/allUsers")
    private List<User> getAllUsers() {
        logger.info("Search list of all users");
        return userService.getAllUsers();
    }

    @PutMapping("/userPreferences")
    public ResponseEntity<String> updateUserPreferences(@RequestParam String userName, @RequestBody UserPreferencesDto userPreferencesDto){
        logger.debug("Update user preferences of {} request ",userName);
        userService.updateUserPreferences(userName, userPreferencesDto);
        return ResponseEntity.status(HttpStatus.OK).body("user preferences saved successfully !!");
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {
        logger.debug("Add user:{} request ",userDto.getUserName());
        userService.addUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("user saved successfully !!");
    }

    @PutMapping("/tripDeals")
    public ResponseEntity<String> updateTripDeals(@RequestParam String userName, @RequestBody List<Provider> tripDeals){
        userService.updateTripDeals(userName, tripDeals);
        return ResponseEntity.status(HttpStatus.OK).body("tripDeals saved successfully !!");
    }
}
