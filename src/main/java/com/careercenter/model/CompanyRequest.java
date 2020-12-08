package com.careercenter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequest {

    @Size(min = 2, max = 50, message = "Name must be at least 2 characters.")
    @NotNull(message = "Name cannot be null.")
    private String name;

    @Size(max = 50)
    @NotNull(message = "City cannot be null")
    private String city;

    @Pattern(regexp = "([A-Z]{2})")
    @NotNull(message = "State cannot be null")
    private String state;
}
