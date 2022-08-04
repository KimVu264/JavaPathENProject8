package tourGuide.service;

import common.dto.UserDto;
import common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService    userService;
    private User user;
    private UserDto userDTO;
    private VisitedLocation visitedLocation;
    private Location location;
    private UserReward userReward;
    private Attraction attraction;

    @BeforeEach
    void setUp() {
        user             = new User(UUID.randomUUID(), "john", "123445", "john@tourguide.com");
        userDTO             = new UserDto( "john", "123445", "john@tourguide.com");
        location         = new Location(33.817595D, -117.922008D);
        visitedLocation  = new VisitedLocation(UUID.randomUUID(), location, new Date());
        attraction = new Attraction(33.817595D, -117.922008D, "Disneyland", "Anaheim", "CA", UUID.randomUUID());
        userReward = new UserReward(visitedLocation, attraction, 22);
    }
    @Test
    void getAllUsersTest() {
        //Arrange
        doReturn(List.of(user)).when(userRepository).findAllUsers();
        //Act
        List<User> users = userService.getAllUsers();
        //Assert
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void addUserTest() {
        //Arrange
        doReturn(null).when(userRepository).findByUserName(any(String.class));
        doNothing().when(userRepository).saveUser(any(User.class));
        //Act
        userService.addUser(userDTO);
        //Assert
        verify(userRepository).saveUser(any());

    }

    @Test
    void getUserTest() {
        //Arrange
        doReturn(user).when(userRepository).findByUserName(any(String.class));
        //Act
        User result = userService.getUser("john");
        //Assert
        assertThat(result).isEqualToComparingFieldByField(user);

    }

}
