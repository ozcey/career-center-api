package com.careercenter.repositories;

import com.careercenter.model.OtherIndustry;
import com.careercenter.model.VolunteerLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtherIndustryRepository extends JpaRepository<OtherIndustry, Long> {

    List<OtherIndustry> findIndustryByVolunteerId(Long volunteerId);
}
