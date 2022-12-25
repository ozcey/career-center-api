package com.careercenter.integration;

import com.careercenter.model.LoginRequest;
import com.careercenter.model.SignUpRequest;
import com.careercenter.repositories.UserRepository;

import com.careercenter.services.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerIntegrationTests extends AbstractContainerBaseTest{

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;
    private SignUpRequest signUpRequest;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();

        Set<String> roles = new HashSet<>(Arrays.asList("ROLE_USER"));
        signUpRequest = SignUpRequest.builder()
                .name("Joe Doe")
                .email("joe@gmail.com")
                .username("joe")
                .password("password")
                .roles(roles)
                .build();
    }

    @Test
    void signUpTest() throws Exception {
        ResultActions response = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User registered successfully.")))
                .andDo(print());
    }

    @Test
    void loginTest() throws Exception {
        authService.signup(signUpRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(signUpRequest.getUsername());
        loginRequest.setPassword(signUpRequest.getPassword());

        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        String content = response.andReturn().getResponse().getContentAsString();
        Map<String, String> loginResponse = objectMapper.readValue(content, new TypeReference<Map<String, String>>() {});
        System.out.println("token " + loginResponse.get("token"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.tokenType", is("Bearer")))
                .andDo(print());
    }
}
