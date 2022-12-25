package com.careercenter.integration;

import com.careercenter.entities.Role;
import com.careercenter.entities.User;
import com.careercenter.exception.NotFoundException;
import com.careercenter.model.RoleName;
import com.careercenter.repositories.RoleRepository;
import com.careercenter.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTests extends AbstractContainerBaseTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        setUsers();
    }

    private void setUsers(){
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(NotFoundException::new);
        HashSet<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        User adminUser = User.builder()
                .name("Admin User")
                .username("admin")
                .email("admin@gmail.com")
                .password("password")
                .roles(adminRoles)
                .build();
        Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(NotFoundException::new);
        HashSet<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        User user = User.builder()
                .name("Seed User")
                .username("seed")
                .email("seed@gmail.com")
                .password("password")
                .roles(userRoles)
                .build();
        userList = Arrays.asList(adminUser, user);
    }

    @Test
    void retrieveAllUsers() throws Exception {
        userRepository.saveAll(userList);

        ResultActions response = mockMvc.perform(get("/users")
                        .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(userList.size())))
                .andDo(print());
    }

    @Test
    void retrieveUserById() throws Exception {
        User savedUser = userRepository.save(userList.get(0));

        ResultActions response = mockMvc.perform(get("/users/id/{id}", savedUser.getId())
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(savedUser.getEmail())))
                .andDo(print());
    }

    @Test
    void retrieveUserByEmail() throws Exception {
        User savedUser = userRepository.save(userList.get(0));

        ResultActions response = mockMvc.perform(get("/users/email/{email}", savedUser.getEmail())
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(savedUser.getEmail())))
                .andDo(print());
    }

    @Test
    void retrieveUserByUsername() throws Exception {
        User savedUser = userRepository.save(userList.get(0));

        ResultActions response = mockMvc.perform(get("/users/username/{username}", savedUser.getUsername())
                .with(user("admin"))
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

        ResultActions response = mockMvc.perform(put("/users/update")
                .with(user("admin"))
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

        ResultActions response = mockMvc.perform(delete("/users/delete/{id}", savedUser.getId())
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("User with id: %s deleted successfully.", savedUser.getId()))))
                .andDo(print());
    }
}