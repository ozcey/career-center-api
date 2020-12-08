package com.careercenter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantRequest {

    @Size(min = 2, max = 50, message = "First Name must be at least 2 characters.")
    @NotNull(message = "First Name cannot be null.")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last Name must be at least 2 characters.")
    @NotNull(message = "Last Name cannot be null.")
    private String lastName;

    @Email(message = "Invalid email address.")
    private String email;

    @Pattern(regexp = "^\\d{10}$")
    @NotNull(message = "Phone cannot be null.")
    private String phone;

    private String[] category;

    @NotNull(message = "Age cannot be null.")
    private int age;

    @Pattern(regexp = "Female|Male")
    @NotNull(message = "Gender cannot be null.")
    private String gender;

    @NotNull(message = "Degree cannot be null.")
    private String degree;

    private String[] languages;

    @Valid
    @NotNull
    private AddressRequest address;
}
