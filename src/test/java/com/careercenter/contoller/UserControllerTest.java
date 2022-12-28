package com.careercenter.contoller;

import com.careercenter.entities.User;
import com.careercenter.exception.NotFoundException;
import com.careercenter.integration.IntegrationTestData;
import com.careercenter.model.ResponseMessage;
import com.careercenter.model.UserResponse;
import com.careercenter.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@WithMockUser(username = "admin", password = "password", roles = "ADMIN")
class UserControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        userList = IntegrationTestData.setUsersWithoutRoles();
    }

    @Test
    void initialTest(){
        ServletContext servletContext = webApplicationContext.getServletContext();
        Assertions.assertNotNull(servletContext);

    }

    @Test
    void retrieveAllUsers() throws Exception {
        List<UserResponse> userResponseList = userList.stream().map((user) -> {
            return UserResponse.builder()
                    .name(user.getName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }).collect(Collectors.toList());

        given(userService.findAllUsers()).willReturn(userResponseList);

        ResultActions response = mockMvc.perform(get("/users")
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(userResponseList.size())));

    }

    @Test
    void retrieveUserById() throws Exception {
        User user = userList.get(0);
        long userId = 1L;
        given(userService.findUserById(userId)).willReturn(user);

        ResultActions response = mockMvc.perform(get("/users/id/{id}", userId)
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void retrieveUserByIdNotFound() throws Exception {
        long userId = 1L;
        given(userService.findUserById(userId)).willThrow(new NotFoundException());

        ResultActions response = mockMvc.perform(get("/users/id/{id}", userId)
        );

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("The data does not match any existing records.")));
    }

    @Test
    void retrieveUserByEmail() throws Exception {
        User user = userList.get(0);
        given(userService.findUserByEmail(user.getEmail())).willReturn(user);

        ResultActions response = mockMvc.perform(get("/users/email/{email}", user.getEmail())
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void retrieveUserByUsername() throws Exception {
        User user = userList.get(0);
        given(userService.findUserByUsername(user.getUsername())).willReturn(user);

        ResultActions response = mockMvc.perform(get("/users/username/{username}", user.getUsername())
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        User user = userList.get(0);
        user.setName("Mike Scott");
        user.setPassword("12345678");

        given(userService.updateUser(ArgumentMatchers.any(User.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        ResultActions response = mockMvc.perform(put("/users/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andDo(print());

    }

    @Test
    void updateUserNotFound() throws Exception {
        long userId = 1L;

        User user = userList.get(0);
        user.setName("Mike Scott");
        user.setPassword("12345678");

        given(userService.updateUser(ArgumentMatchers.any(User.class)))
                .willThrow(new NotFoundException());

        ResultActions response = mockMvc.perform(put("/users/update")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(user))
        );

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("The data does not match any existing records.")));
    }

    @Test
    void deleteUser() throws Exception {
        long userId = 1L;
        ResponseMessage message = new ResponseMessage(String.format("User with id: %s deleted successfully.", userId));

        given(userService.deleteUser(userId)).willReturn(message);

        ResultActions response = mockMvc.perform(delete("/users/delete/{id}", userId));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("User with id: %s deleted successfully.", userId))))
                .andDo(print());
    }

}