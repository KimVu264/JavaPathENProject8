package tourGuide.service;

import common.dto.UserDto;
import common.model.Provider;
import common.model.User;
import common.model.UserPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.exception.UserExisted;
import tourGuide.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        logger.info("Get all internalUserMap users");
        return userRepository.findAllUsers();
    }
    public User getUser(String userName) {
        logger.info("Get internalUserMap with user: {}",userName);
        return userRepository.findByUserName(userName);
    }

    public User getUserById(UUID userId) {
        logger.info("Get internalUserMap with user: {}",userId);
        return userRepository.findById(userId);
    }
    public void addUser(UserDto userDto) throws UserExisted {
        logger.debug("get user {}", userDto.getUserName());
        User user = userRepository.findByUserName(userDto.getUserName());
        if (user != null) {
            throw new UserExisted();
        }
    }


    public void updateUserPreferences(String userName, UserPreferences userPreferences)  {
        logger.debug("get user {}", userName);
        User user = getUser(userName);
        user.setUserPreferences(userPreferences);
    }

    public void updateTripDeals(String userName, List<Provider> tripDeals) {
        User user = getUser(userName);
        user.setTripDeals(tripDeals);
    }
}
