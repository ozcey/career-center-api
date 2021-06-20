package com.careercenter.services;

import com.careercenter.entities.Role;
import com.careercenter.entities.User;
import com.careercenter.exception.NotFoundException;
import com.careercenter.model.*;
import com.careercenter.repositories.RoleRepository;
import com.careercenter.repositories.UserRepository;
import com.careercenter.security.JWTTokenProvider;
import com.careercenter.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtProvider;

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest){
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJWTToken(authentication);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    public ResponseEntity<ResponseMessage> signup(SignUpRequest signUpRequest){
        checkUsernameAndEmailInUse(signUpRequest);
        User user = getUser(signUpRequest);
        Set<String> sRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        setRoles(sRoles, roles);
        user.setRoles(roles);
        userRepository.save(user);
        log.info("User created successfully {}", user.toString());
        return ResponseEntity.ok(new ResponseMessage(Constants.SignupMessage.getName()));
    }

    private ResponseEntity<ResponseMessage> checkUsernameAndEmailInUse(SignUpRequest signUpRequest) {
        ResponseMessage responseMessage = new ResponseMessage();
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            responseMessage.setMessage(Constants.UsernameMessage.getName());
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            responseMessage.setMessage(Constants.EmailMessage.getName());
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    private void setRoles(Set<String> sRoles, Set<Role> roles) {
        sRoles.forEach(role -> {
            if (Constants.RoleAdmin.getName().equals(role)) {
                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new NotFoundException(Constants.UserRole.getName()));
                roles.add(adminRole);
            } else if (Constants.RoleUser.getName().equals(role)) {
                Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new NotFoundException(Constants.UserRole.getName()));
                roles.add(userRole);
            } else {
                throw new NotFoundException(Constants.UserRole.getName());
            }
        });
    }

    private User getUser(SignUpRequest signUpRequest) {
        return User.builder().name(signUpRequest.getName()).username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail()).password(passwordEncoder.encode(signUpRequest.getPassword())).build();
    }
}
