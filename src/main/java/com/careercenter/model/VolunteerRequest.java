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
public class VolunteerRequest {

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

    @NotNull(message = "Job Title cannot be null.")
    private String jobTitle;

    @NotNull(message = "Industry cannot be null.")
    private String industry;

    private String[] otherIndustries;

    @NotNull(message = "Years of experience cannot be null.")
    private int yearsOfExperience;

    private String[] languages;

    @Valid
    @NotNull
    private AddressRequest address;
}
