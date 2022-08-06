package tourGuide.service;

import common.dto.UserDto;
import common.dto.UserLocationDto;
import common.dto.UserPreferencesDto;
import common.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        //logger.info("Get all internalUserMap users");
        return userRepository.findAllUsers();
    }
    public User getUser(String userName) {
        logger.info("Get internalUserMap with user: {}",userName);
        return userRepository.findByUserName(userName);
    }

    public void addUser(UserDto userDto)  {
        logger.debug("get user {}", userDto.getUserName());
        User userToSave = userRepository.findByUserName(userDto.getUserName());
        if (userToSave != null) {
           logger.error("User with username : " + userDto.getUserName() + " already exists ");
        }
        userRepository.saveUser(new User(UUID.randomUUID(), userDto.getUserName(), userDto.getPhoneNumber(), userDto.getEmailAddress()));
    }

    public void addUserReward(String userName, UserReward userReward) {
        userRepository.addUserReward(userName,userReward);
    }

    public List<UserLocationDto> getAllCurrentLocations() {
        List<UserLocationDto> userLocationsList = new ArrayList<>();
        for (User user : getAllUsers()) {
            UUID userId = user.getUserId();
            VisitedLocation userLastVisitedLocation = user.getLastVisitedLocation();
            userLocationsList.add(new UserLocationDto(userId, userLastVisitedLocation.getLocation()));
            user.addToVisitedLocations(userLastVisitedLocation);
        }
        logger.info("Get all current location for all users");
        return userLocationsList;
    }

    public void updateUserPreferences(String userName, UserPreferencesDto userPreferencesDto)  {
        logger.debug("get user {}", userName);
        User user = getUser(userName);
        user.setUserPreferences(new UserPreferences(userPreferencesDto.getTripDuration(), userPreferencesDto.getTicketQuantity(), userPreferencesDto.getNumberOfAdults(), userPreferencesDto.getNumberOfChildren()));
    }


    public void updateTripDeals(String userName, List<Provider> tripDeals) {
        User user = getUser(userName);
        user.setTripDeals(tripDeals);
    }
}
