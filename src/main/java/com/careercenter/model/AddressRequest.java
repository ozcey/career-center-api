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
public class AddressRequest {

    @Size(max = 100)
    @NotNull(message = "Street cannot be null")
    private String street;

    @Size(max = 50)
    @NotNull(message = "City cannot be null")
    private String city;

    @Pattern(regexp = "([A-Z]{2})")
    @NotNull(message = "State cannot be null")
    private String state;

    @Pattern(regexp = "^\\d{5}(?:[-\\s]\\d{4})?$")
    @NotNull(message = "Zip code cannot be null")
    private String zipcode;
}
