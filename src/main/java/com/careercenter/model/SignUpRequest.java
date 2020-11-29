package com.careercenter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

	@NotBlank
	@Size(min = 2, max = 50, message = "At least 2 characters long.")
	private String name;

	@NotBlank
	@Size(min = 3, max = 15)
	private String username;

	@NotBlank
	@Email
	@Size(max = 50)
	private String email;

	@NotBlank
	@Size(min = 8, max = 20)
	private String password;

	private Set<String> roles;

}
