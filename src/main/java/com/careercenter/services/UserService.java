package com.careercenter.services;

import com.careercenter.exception.NotFoundException;
import com.careercenter.mapper.UserMapper;
import com.careercenter.model.ResponseMessage;
import com.careercenter.entities.User;
import com.careercenter.model.UserResponse;
import com.careercenter.repositories.UserRepository;
import com.careercenter.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream().map(userMapper::getUserResponse).collect(Collectors.toList());
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(Constants.Email.getName()));
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.Username.getName()));
    }

    public User findUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.UserID.getName()));
    }

    public User updateUser(User user) {
        log.info("User update request received.");
        if (userRepository.existsById(user.getId())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new NotFoundException(Constants.User.getName());
    }

    public ResponseMessage deleteUser(Long  userId) {
        log.info("User delete request received.");
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return new ResponseMessage(String.format("%s %s", Constants.UserID.getName(), Constants.DeleteMessage.getName()));
        }
        throw new NotFoundException(Constants.UserID.getName());
    }

}
