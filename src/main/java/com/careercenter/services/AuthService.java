package com.careercenter.services;

import com.careercenter.entities.Role;
import com.careercenter.entities.User;
import com.careercenter.exception.NotFoundException;
import com.careercenter.mapper.UserMapper;
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
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTTokenProvider jwtProvider;
    private final UserMapper userMapper;

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        log.info("Login request received.");
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJWTToken(authentication);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    public ResponseEntity<ResponseMessage> signup(SignUpRequest signUpRequest) {
        log.info("Sign up request received.");
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage(Constants.UsernameMessage.getName()), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage(Constants.EmailMessage.getName()), HttpStatus.BAD_REQUEST);
        }

        Set<String> sRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        setRoles(sRoles, roles);
        Optional<User> optionalUser = userMapper.getAuthUser(signUpRequest);
        return optionalUser.map(user -> {
            user.setRoles(roles);
            userRepository.save(user);
            log.info("User registered successfully");
            return ResponseEntity.ok(new ResponseMessage(Constants.SignupMessage.getName()));
        }).orElseThrow(NotFoundException::new);
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
}
