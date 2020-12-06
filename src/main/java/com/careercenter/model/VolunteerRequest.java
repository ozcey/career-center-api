package com.careercenter.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class VolunteerRequest {

    @NotNull(message = "Volunteer cannot be null.")
    private Volunteer volunteer;

    @NotNull(message = "Company cannot be null.")
    private List<Company> companies;

}
