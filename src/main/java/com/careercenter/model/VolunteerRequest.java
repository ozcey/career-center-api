package com.careercenter.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
public class VolunteerRequest {

    @NotNull(message = "First Name cannot be null.")
    private String firstName;

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
    @Column(name = "industry")
    private String industry;

    @NotNull(message = "Years of experience cannot be null.")
    private int yearsOfExperience;

    @NotNull(message = "Volunteer Language cannot be null.")

    private List<String> languages;

    @NotNull(message = "Other Industry cannot be null.")
    private List<String> otherIndustry;

    @NotNull(message = "Company cannot be null.")
    private Company company;

    @NotNull(message = "Address cannot be null.")
    private Address address;
}