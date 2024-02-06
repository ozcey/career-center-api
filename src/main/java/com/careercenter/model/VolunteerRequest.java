package com.careercenter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    @Size(min = 2, max = 50, message = "Name must be at least 2 characters.")
    @NotNull(message = "Name cannot be null.")
    private String name;


    @Email(message = "Invalid email address.")
    private String email;

    @Pattern(regexp = "^\\d{10}$")
    @NotNull(message = "Phone cannot be null.")
    private String phone;

    @NotNull(message = "Job Title cannot be null.")
    private String jobTitle;

    @NotNull(message = "Industry cannot be null.")
    private String industry;
    
	@NotNull(message = "Area of Interest cannot be null.")
	private String areaOfInterest;

}
