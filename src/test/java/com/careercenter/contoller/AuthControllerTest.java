package com.careercenter.contoller;

import com.careercenter.model.LoginRequest;
import com.careercenter.model.LoginResponse;
import com.careercenter.model.ResponseMessage;
import com.careercenter.model.SignUpRequest;
import com.careercenter.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void login() throws Exception {
        LoginResponse loginResponse = new LoginResponse("1234567890");
        ResponseEntity<LoginResponse> responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");

        willReturn(responseEntity).given(authService).login(loginRequest);

        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(loginResponse.getToken())))
                .andExpect(jsonPath("$.tokenType", is(loginResponse.getTokenType())))
                .andDo(print());
    }

    @Test
    void signup() throws Exception {
        Set<String> roles = new HashSet<>(Arrays.asList("ROLE_USER"));
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .name("Joe Doe")
                .email("joe@gmail.com")
                .username("joe")
                .password("password")
                .roles(roles)
                .build();

        ResponseMessage message = new ResponseMessage("User registered successfully.");
        ResponseEntity<ResponseMessage> responseEntity =  new ResponseEntity<>(message, HttpStatus.OK);

        willReturn(responseEntity).given(authService).signup(signUpRequest);

        ResultActions response = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(message.getMessage())))
                .andDo(print());
    }
}