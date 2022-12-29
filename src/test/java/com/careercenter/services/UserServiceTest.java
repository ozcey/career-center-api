package com.careercenter.services;

import com.careercenter.entities.User;
import com.careercenter.exception.NotFoundException;
import com.careercenter.integration.IntegrationTestData;
import com.careercenter.mapper.UserMapper;
import com.careercenter.model.ResponseMessage;
import com.careercenter.model.UserResponse;
import com.careercenter.repositories.UserRepository;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        userList = IntegrationTestData.setUsersWithoutRoles();
    }

    @Test
    void findAllUsers() {
        List<UserResponse> userResponseList = userList.stream().map((user) -> {
            return UserResponse.builder()
                    .name(user.getName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }).collect(Collectors.toList());

        given(userRepository.findAll()).willReturn(userList);
        given(userMapper.getUserResponse(userList.get(0))).willReturn(userResponseList.get(0));

        List<UserResponse> users = userService.findAllUsers();

        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(userList.size());
    }

    @Test
    void findUserByEmail() {
        User user = userList.get(0);

        given(userRepository.findUserByEmail(user.getEmail())).willReturn(Optional.of(user));
        User returnedUser = userService.findUserByEmail(user.getEmail());

        assertThat(returnedUser).isNotNull();
        assertThat(returnedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findUserByEmailThrowsNotFound() {
        User user = userList.get(0);

        given(userRepository.findUserByEmail(user.getEmail())).willThrow(new NotFoundException());

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.findUserByEmail(user.getEmail());
        });
        verify(userRepository, times(1)).findUserByEmail(user.getEmail());
    }

    @Test
    void findUserByUsername() {
        User user = userList.get(0);

        given(userRepository.findUserByUsername(user.getUsername())).willReturn(Optional.of(user));
        User returnedUser = userService.findUserByUsername(user.getUsername());

        assertThat(returnedUser).isNotNull();
        assertThat(returnedUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void findUserByUsernameThrowsNotFound() {
        User user = userList.get(0);

        given(userRepository.findUserByUsername(user.getUsername())).willThrow(new NotFoundException());

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.findUserByUsername(user.getUsername());
        });
        verify(userRepository, times(1)).findUserByUsername(user.getUsername());
    }

    @Test
    void findUserById() {
        User user = userList.get(0);
        user.setId(1L);
        given(userRepository.findUserById(user.getId())).willReturn(Optional.of(user));
        User returnedUser = userService.findUserById(user.getId());

        assertThat(returnedUser).isNotNull();
        assertThat(returnedUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void findUserByIdThrowsNotFound() {
        User user = userList.get(0);

        given(userRepository.findUserById(user.getId())).willThrow(new NotFoundException());

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.findUserById(user.getId());
        });
        verify(userRepository, times(1)).findUserById(user.getId());
    }

    @Test
    void updateUser() {
        User user = userList.get(0);
        user.setId(1L);

        User updatedUser = User.builder()
                .id(user.getId())
                .name("Mike Scott")
                .username(user.getUsername())
                .email(user.getEmail())
                .password("12345678")
                .build();

        given(userRepository.existsById(user.getId())).willReturn(true);
        given(passwordEncoder.encode(user.getPassword())).willReturn("12345678");
        given(userRepository.save(user)).willReturn(updatedUser);
        User returnedUser = userService.updateUser(user);

        assertThat(returnedUser).isNotNull();
        assertThat(returnedUser.getName()).isEqualTo(updatedUser.getName());
        assertThat(returnedUser.getPassword()).isEqualTo(updatedUser.getPassword());
    }

    @Test
    void deleteUser() {
        User user = userList.get(0);
        user.setId(1L);

        given(userRepository.existsById(user.getId())).willReturn(true);
        willDoNothing().given(userRepository).deleteById(user.getId());
        ResponseMessage message = userService.deleteUser(user.getId());

        assertThat(message).isNotNull();
        assertThat(message.getMessage()).isEqualTo(String.format("User with id: %s deleted successfully.", user.getId()));
    }
}