package com.careercenter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.careercenter.model.Applicant;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

	@Query("select a from Applicant a where a.id = :id")
	Optional<Applicant> findApplicantById(@Param("id") Long id);

	@Query("select a from Applicant a where a.email = :email")
	Optional<Applicant> findApplicantByEmail(@Param("email") String email);

}
