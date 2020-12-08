package com.careercenter.model;

import java.util.List;

import com.careercenter.entities.Company;
import com.careercenter.entities.Volunteer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class VolunteerResponse {

	private Volunteer volunteer;
	private List<Company> companies;
}
