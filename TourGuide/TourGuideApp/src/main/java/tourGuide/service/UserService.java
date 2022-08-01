package tourGuide.service;

import common.dto.UserPreferencesDto;
import common.model.User;
import common.model.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    Map<String, User> internalUserMap = new HashMap<>();

    public User getUser(String userName) {
        logger.info("Get internalUserMap with user: {}",userName);
        return internalUserMap.get(userName);
    }
    public List<User> getAllUsers() {
        logger.info("Get all internalUserMap users");
        return new ArrayList<>(internalUserMap.values());
    }

    public void updateUserPreferences(String userName, UserPreferences userPreferences)  {
        logger.debug("get user {}", userName);
        User user = getUser(userName);
        user.setUserPreferences(new UserPreferences(userPreferences.getTripDuration(), userPreferences.getTicketQuantity(), userPreferences.getNumberOfAdults(), userPreferences.getNumberOfChildren()));
    }

    public void addUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
            logger.info("{} have been added.",user.getUserName());
        }
    }
}
