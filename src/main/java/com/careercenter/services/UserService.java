package com.careercenter.services;

import com.careercenter.exception.NotFoundException;
import com.careercenter.model.ResponseMessage;
import com.careercenter.model.User;
import com.careercenter.repositories.UserRepository;
import com.careercenter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAllUsers() {
        return userRepository.findAll().stream().map(user -> User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .build()).collect(Collectors.toList());
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(Utils.Email.getName()));
    }

    public User findUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(Utils.UserID.getName()));
    }

    public User updateUser(User user) {
        if (userRepository.existsById(user.getId())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new NotFoundException(Utils.User.getName());
    }

    public ResponseMessage deleteUser(Long  userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return new ResponseMessage(String.format("%s %s", Utils.UserID.getName(), Utils.DeleteMessage.getName()));
        }
        throw new NotFoundException(Utils.UserID.getName());
    }


}
