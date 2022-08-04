package tourGuide.repository;

import common.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {

    private Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final Map<String, User> internalUserMap = new HashMap<>();

    public User findByUserName(String userName) {

        return internalUserMap.get(userName);
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(internalUserMap.values());
    }

    public void saveUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    public void deleteAll() {
        this.internalUserMap.clear();
    }
}
