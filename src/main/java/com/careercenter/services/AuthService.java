package com.careercenter.services;

import com.careercenter.exception.NotFoundException;
import com.careercenter.model.*;
import com.careercenter.repositories.RoleRepository;
import com.careercenter.repositories.UserRepository;
import com.careercenter.security.JWTTokenProvider;
import com.careercenter.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthService {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JWTTokenProvider jwtProvider;

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest){
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJWTToken(authentication);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    public ResponseEntity<ResponseMessage> signup(SignUpRequest signUpRequest){
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage(Utils.UsernameMessage.getName()), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage(Utils.EmailMessage.getName()), HttpStatus.BAD_REQUEST);
        }

        User user = User.builder().name(signUpRequest.getName()).username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail()).password(passwordEncoder.encode(signUpRequest.getPassword())).build();

        Set<String> sRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        sRoles.forEach(role -> {
            if (Utils.RoleAdmin.getName().equals(role)) {
                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new NotFoundException(Utils.UserRole.getName()));
                roles.add(adminRole);
            } else if (Utils.RoleUser.getName().equals(role)) {
                Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new NotFoundException(Utils.UserRole.getName()));
                roles.add(userRole);
            } else {
                throw new NotFoundException(Utils.UserRole.getName());
            }
        });
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseMessage(Utils.SignupMessage.getName()));
    }
}
