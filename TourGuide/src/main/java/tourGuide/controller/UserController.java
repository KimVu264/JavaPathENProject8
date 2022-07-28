package tourGuide.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.model.User;
import tourGuide.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);
    @Autowired
    UserService userService;

    @RequestMapping("/allUsers")
    private List<User> getAllUsers() {
        logger.info("Search list of all users");
        return userService.getAllUsers();
    }

    @PostMapping("/userPreferences")
    public ResponseEntity<String> updateUserPreferences(@RequestParam String userName, @RequestBody UserPreferencesDto userPreferences){
        logger.debug("Update user preferences of {} request ",userName);
        userService.updateUserPreferences(userName, userPreferences);
        return ResponseEntity.status(HttpStatus.OK).body("user preferences saved successfully !!");
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody User user){
        logger.info("Call service for add user: {}", user);
        userService.addUser(user);
    }
}
