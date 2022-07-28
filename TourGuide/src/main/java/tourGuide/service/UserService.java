package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.helper.InternalTestingData;
import tourGuide.model.User;
import tourGuide.model.UserPreferences;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    public InternalTestingData internalTestingData;

    public User getUser(String userName) {
        return internalTestingData.internalUserMap.get(userName);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(internalTestingData.internalUserMap.values());
    }

    public void updateUserPreferences(String userName, UserPreferencesDto userPreferences)  {
        logger.debug("get user {}", userName);
        User user = getUser(userName);
        user.setUserPreferences(new UserPreferences(userPreferences.getTripDuration(), userPreferences.getTicketQuantity(), userPreferences.getNumberOfAdults(), userPreferences.getNumberOfChildren()));
    }

    public void addUser(User user) {
        if(!internalTestingData.internalUserMap.containsKey(user.getUserName())) {
            internalTestingData.internalUserMap.put(user.getUserName(), user);
        }
    }
}
