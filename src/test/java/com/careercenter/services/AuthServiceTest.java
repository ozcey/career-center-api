package com.careercenter.services;

import com.careercenter.entities.Role;
import com.careercenter.entities.User;
import com.careercenter.integration.IntegrationTestData;
import com.careercenter.mapper.UserMapper;
import com.careercenter.model.*;
import com.careercenter.repositories.RoleRepository;
import com.careercenter.repositories.UserRepository;
import com.careercenter.security.JWTTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private JWTTokenProvider jwtTokenProvider;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private AuthService authService;
    private List<User> userList;

    @Test
    void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");

        LoginResponse loginResponse = new LoginResponse("1234567890");

        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        given(authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())))
                .willReturn(authentication);
        given(jwtTokenProvider.generateJWTToken(authentication)).willReturn(loginResponse.getToken());

        ResponseEntity<LoginResponse> response = authService.login(loginRequest);

        assertThat(response.getBody().getToken()).isEqualTo(loginResponse.getToken());

    }

    @Test
    void signup() {
        Set<String> roles = new HashSet<>(Arrays.asList("ROLE_ADMIN"));
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .name("Joe Doe")
                .email("joe@gmail.com")
                .username("joe")
                .password("password")
                .roles(roles)
                .build();

        Role role = Role.builder().name(RoleName.ROLE_ADMIN).build();
        userList = IntegrationTestData.setUsersWithoutRoles();
        User user = userList.get(0);

        ResponseMessage message = new ResponseMessage("User registered successfully.");
        ResponseEntity<ResponseMessage> responseEntity =  new ResponseEntity<>(message, HttpStatus.OK);

        given(userRepository.existsByUsername(signUpRequest.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(signUpRequest.getEmail())).willReturn(false);
        given(roleRepository.findByName(RoleName.ROLE_ADMIN)).willReturn(Optional.of(role));
        given(userMapper.getAuthUser(signUpRequest)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        ResponseEntity<ResponseMessage> response = authService.signup(signUpRequest);

        assertThat(response.getBody().getMessage()).isEqualTo(message.getMessage());

    }
}