package com.careercenter.services;

import java.util.List;
import java.util.Optional;

import com.careercenter.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.careercenter.exception.NotFoundException;
import com.careercenter.model.Applicant;
import com.careercenter.model.ResponseMessage;
import com.careercenter.repositories.ApplicantRepository;

@Slf4j
@Service
public class ApplicantService {

	@Autowired
	private ApplicantRepository applicantRepository;

	public List<Applicant> findAllApplicants() {
		return applicantRepository.findAll();
	}

	public Applicant findApplicantById(Long applicantDd) {
		return applicantRepository.findApplicantById(applicantDd)
				.orElseThrow(() -> new NotFoundException(Utils.ApplicantID.getName()));
	}

	public Applicant findApplicantByEmail(String email) {
		return applicantRepository.findApplicantByEmail(email).orElseThrow(() -> new NotFoundException(Utils.Email.getName()));
	}

	public Applicant saveApplicant(Applicant applicant) {
		log.info("Applicant save request received.");
		Optional<Applicant> optionalApplicant = Optional.ofNullable(applicant);
		if (optionalApplicant.isPresent()) {
			return applicantRepository.save(applicant);
		}
		throw new NotFoundException();
	}

	public ResponseMessage deleteApplicant(Long applicantId) {
		log.info("Applicant delete request received.");
		if (applicantRepository.existsById(applicantId)) {
			applicantRepository.deleteById(applicantId);
			return new ResponseMessage(String.format("%s %s", Utils.ApplicantID.getName(), Utils.DeleteMessage.getName()));

		}
		throw new NotFoundException(Utils.ApplicantID.getName());
	}

}
