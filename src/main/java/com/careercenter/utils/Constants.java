package com.careercenter.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Constants {
    Authorization("Authorization"),
    RoleAdmin("ROLE_ADMIN"),
    RoleUser("ROLE_USER"),
    UserRole("User role"),
    EmailMessage("Email is already in use!"),
    UsernameMessage("Username is already taken!"),
    SignupMessage("User registered successfully."),
    DeleteMessage("deleted successfully."),
    Email("email"),
    User("user"),
    UserID("userId"),
    CompanyID("companyId"),
    ApplicantID("applicantId"),
    VolunteerID("volunteerId"),
    ServerError("Server Error");

    private final String name;
}
