package com.careercenter.repositories;

import com.careercenter.model.ApplicantLanguage;
import com.careercenter.model.VolunteerLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ALanguageRepository extends JpaRepository<ApplicantLanguage, Long> {

    List<ApplicantLanguage> findVLanguageByApplicantId(Long applicantId);
}
