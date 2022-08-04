package tourGuide.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.UserDto;
import common.dto.UserPreferencesDto;
import common.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.controller.UserController;
import tourGuide.service.UserService;

import java.util.ArrayList;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void getAllUsersTest() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/allUsers")).andExpect(status().isOk());
    }

    @Test
    void addUserTest() throws Exception {
        //Arrange
        UserDto user = new UserDto("john", "898", "john@tourguide.com");

        //Act
        mockMvc.perform(post("/addUser")
                        .content(asJsonString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                      )

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(
                        "user saved successfully")));
    }

    @Test
    void updateUserPreferences() throws Exception {

        //Arrange
        UserPreferencesDto userPreferencesDto = new UserPreferencesDto(3, 3, 2, 1);
        //Act
        mockMvc.perform(put("/userPreferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userName", "kim")
                        .content(asJsonString(userPreferencesDto))
                        .accept(MediaType.APPLICATION_JSON)

                        )

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "user preferences saved successfully !!")));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
