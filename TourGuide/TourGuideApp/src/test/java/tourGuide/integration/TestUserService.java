package tourGuide.integration;

import common.dto.UserDto;
import common.dto.UserPreferencesDto;
import common.model.Provider;
import common.model.User;
import common.model.UserPreferences;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.repository.UserRepository;
import tourGuide.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestUserService {

    @Autowired
    private UserService userService;

    UserDto user1 = new UserDto("john", "000", "jon@tourGuide.com");;
    UserDto user2 = new UserDto( "jonny", "000", "jonny@tourGuide.com");
    User user3 = new User(UUID.randomUUID(), "kim", "000", "kim@tourguide.com");

    @Test
    void getAllUsersTest() {
        userService.addUser(user1);
        userService.addUser(user2);

        List<User> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertNotEquals(0, allUsers.size());
    }


    @Test
    void getUserTest() {
        userService.addUser(user1);

        User user = userService.getUser(user1.getUserName());

        assertNotNull(user);
        assertEquals(user1.getUserName(), user.getUserName());
    }


    @Test
    void updateTripDealsTest(){
        Provider provider = new Provider("name", 200, UUID.randomUUID());
        Provider provider2 = new Provider("nameTest", 300,UUID.randomUUID());
        List<Provider> providerList = new ArrayList<>();

        providerList.add(0,provider);
        providerList.add(1,provider2);
        user3.setTripDeals(providerList);

        assertNotNull(user3.getTripDeals());
        assertNotEquals(0, user3.getTripDeals().size());
        assertNotNull(user3.getTripDeals().get(0).getTripId());

    }
}
