package com.careercenter.mapper;

import com.careercenter.entities.User;
import com.careercenter.model.SignUpRequest;
import com.careercenter.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getAuthUser(SignUpRequest signUpRequest) {
        if(signUpRequest != null){
            User user = User.builder().name(signUpRequest.getName()).username(signUpRequest.getUsername())
                    .email(signUpRequest.getEmail()).password(passwordEncoder.encode(signUpRequest.getPassword())).build();
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public UserResponse getUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        if (user != null) {
            userResponse = UserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build();
        }
        return userResponse;
    }
}
