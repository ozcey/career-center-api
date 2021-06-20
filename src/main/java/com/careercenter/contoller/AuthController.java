package com.careercenter.contoller;

import com.careercenter.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.careercenter.model.LoginRequest;
import com.careercenter.model.LoginResponse;
import com.careercenter.model.ResponseMessage;
import com.careercenter.model.SignUpRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Auth Controller", description = "Auth API")
@ApiResponse(responseCode = "200", description = "Success")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Log in to Account", description = "username = admin and password = password")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create an Account", description = "Password must be at least 8 characters and "
			+ "username must be at least 3 characters ")
	public ResponseEntity<ResponseMessage> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
		return authService.signup(signUpRequest);
	}
}
