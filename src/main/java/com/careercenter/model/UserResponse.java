package com.careercenter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @NotNull
    private Long id;

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
}
