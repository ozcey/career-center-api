package com.careercenter.model;

import com.careercenter.entities.Company;
import com.careercenter.entities.Volunteer;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class SaveVolunteerRequest {

    @Valid
    @NotNull(message = "Volunteer cannot be null.")
    private VolunteerRequest volunteer;

    @Valid
    @NotNull(message = "Company cannot be null.")
    private List<CompanyRequest> companies;

}
