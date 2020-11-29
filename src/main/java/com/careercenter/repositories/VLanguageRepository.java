package com.careercenter.repositories;

import com.careercenter.model.Company;
import com.careercenter.model.VolunteerLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VLanguageRepository extends JpaRepository<VolunteerLanguage, Long> {

    List<VolunteerLanguage> findVLanguageByVolunteerId(Long volunteerId);
}
