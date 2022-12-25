package com.careercenter.contoller;

import com.careercenter.model.*;
import com.careercenter.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Controller", description = "Auth API")
@ApiResponse(responseCode = "200", description = "Success")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Log in to Account", description = "Pass your username and password")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create an Account", description = "Role must be ROLE_USER, Password must be at least 8 characters and "
			+ "username must be at least 3 characters ")
	public ResponseEntity<ResponseMessage> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
		return authService.signup(signUpRequest);
	}
}
