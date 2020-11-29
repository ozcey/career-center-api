package com.careercenter.contoller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.careercenter.exception.NotFoundException;
import com.careercenter.model.LoginRequest;
import com.careercenter.model.LoginResponse;
import com.careercenter.model.ResponseMessage;
import com.careercenter.model.Role;
import com.careercenter.model.RoleName;
import com.careercenter.model.SignUpRequest;
import com.careercenter.model.User;
import com.careercenter.repositories.RoleRepository;
import com.careercenter.repositories.UserRepository;
import com.careercenter.security.JWTTokenProvider;

@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Auth Controller", description = "Auth API")
@ApiResponse(responseCode = "200", description = "Success")
@RestController
@RequestMapping("/auth")
public class AuthController {

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

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Log in to Account", description = "username = admin and password = password")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateJWTToken(authentication);
		return ResponseEntity.ok(new LoginResponse(jwt));
	}

	@PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create an Account", description = "Password must be at least 8 characters and "
			+ "username must be at least 3 characters ")
	public ResponseEntity<ResponseMessage> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<>(new ResponseMessage("Username is already taken!"), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Email is already in use!"), HttpStatus.BAD_REQUEST);
		}

		User user = User.builder().name(signUpRequest.getName()).username(signUpRequest.getUsername())
				.email(signUpRequest.getEmail()).password(passwordEncoder.encode(signUpRequest.getPassword())).build();

		Set<String> sRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		sRoles.forEach(role -> {
			if ("ROLE_ADMIN".equals(role)) {
				Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
						.orElseThrow(() -> new NotFoundException("User role"));
				roles.add(adminRole);
			}
			if ("ROLE_USER".equals(role)) {
				Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
						.orElseThrow(() -> new NotFoundException("User role"));
				roles.add(userRole);
			}
			throw new NotFoundException("User role");
		});
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new ResponseMessage("User registered successfully!"));
	}

}
