package com.careercenter.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
public class VolunteerResponse {

	private Volunteer volunteer;
	private List<Company> companies;
	private List<VolunteerLanguage> languages;
	private List<OtherIndustry> otherIndustries;

}
