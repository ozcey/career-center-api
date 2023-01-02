package com.careercenter.integration;

import com.careercenter.entities.User;
import com.careercenter.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTests extends AbstractContainerBaseTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private List<User> userList;
    private static final String BASE_URI = "/user";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        userList = IntegrationTestData.setUsersWithoutRoles();
    }

    @Test
    void retrieveAllUsers() throws Exception {
        userRepository.saveAll(userList);

        ResultActions response = mockMvc.perform(get(BASE_URI)

                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(userList.size())))
                .andDo(print());
    }

    @Test
    void retrieveUserById() throws Exception {
        User savedUser = userRepository.save(userList.get(0));

        ResultActions response = mockMvc.perform(get(BASE_URI + "/id/{id}", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(savedUser.getEmail())))
                .andDo(print());
    }

    @Test
    void retrieveUserByEmail() throws Exception {
        User savedUser = userRepository.save(userList.get(0));

        ResultActions response = mockMvc.perform(get(BASE_URI + "/email/{email}", savedUser.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(savedUser.getEmail())))
                .andDo(print());
    }

    @Test
    void retrieveUserByUsername() throws Exception {
        User savedUser = userRepository.save(userList.get(0));

        ResultActions response = mockMvc.perform(get(BASE_URI + "/username/{username}", savedUser.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(savedUser.getUsername())))
                .andDo(print());
    }

    @Test
    void updateUser() throws Exception {
        User user = userList.get(0);
        User savedUser = userRepository.save(user);
        savedUser.setName("John Doe");

        ResultActions response = mockMvc.perform(put(BASE_URI + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedUser))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(savedUser.getName())))
                .andDo(print());
    }

    @Test
    void deleteUser() throws Exception {
        User savedUser = userRepository.save(userList.get(0));

        ResultActions response = mockMvc.perform(delete(BASE_URI + "/delete/{id}", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("User with id: %s deleted successfully.", savedUser.getId()))))
                .andDo(print());
    }
}