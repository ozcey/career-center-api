package com.careercenter.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.careercenter.exception.NotFoundException;
import com.careercenter.model.Applicant;
import com.careercenter.model.ResponseMessage;
import com.careercenter.repositories.ApplicantRepository;

@Service
public class ApplicantService {

	@Autowired
	private ApplicantRepository applicantRepository;

	public List<Applicant> findAllApplicants() {
		return applicantRepository.findAll();
	}

	public Applicant findApplicantById(Long applicant_id) {
		return applicantRepository.findApplicantById(applicant_id)
				.orElseThrow(() -> new NotFoundException("applicant_id"));
	}

	public Applicant findApplicantByEmail(String email) {
		return applicantRepository.findApplicantByEmail(email).orElseThrow(() -> new NotFoundException("email"));
	}

	public Applicant saveApplicant(Applicant applicant) {
		Optional<Applicant> optionalApplicant = Optional.ofNullable(applicant);
		if (optionalApplicant.isPresent()) {
			return applicantRepository.save(applicant);
		}
		throw new NotFoundException();
	}

	public ResponseMessage deleteApplicant(Long id) {
		if (applicantRepository.existsById(id)) {
			applicantRepository.deleteById(id);
			return new ResponseMessage("Applicant deleted successfully.");
		}
		throw new NotFoundException("id");
	}

}
