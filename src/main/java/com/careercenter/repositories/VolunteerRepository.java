package com.careercenter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.careercenter.model.Volunteer;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

	@Query("select v from Volunteer v where v.id = :id")
	Optional<Volunteer> findVolunteerById(@Param("id") Long id);

	@Query("select v from Volunteer v where v.email = :email")
	Optional<Volunteer> findVolunteerByEmail(@Param("email") String email);

}
