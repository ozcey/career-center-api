package com.careercenter.services;

import java.util.List;
import java.util.Optional;

import com.careercenter.mapper.ApplicantMapper;
import com.careercenter.model.ApplicantRequest;
import com.careercenter.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.careercenter.exception.NotFoundException;
import com.careercenter.entities.Applicant;
import com.careercenter.model.ResponseMessage;
import com.careercenter.repositories.ApplicantRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantService {

	private final ApplicantRepository applicantRepository;
	private final ApplicantMapper applicantMapper;

	public List<Applicant> findAllApplicants() {
		return applicantRepository.findAll();
	}

	public Applicant findApplicantById(Long applicantDd) {
		return applicantRepository.findApplicantById(applicantDd)
				.orElseThrow(() -> new NotFoundException(Constants.ApplicantID.getName()));
	}

	public Applicant findApplicantByEmail(String email) {
		return applicantRepository.findApplicantByEmail(email).orElseThrow(() -> new NotFoundException(Constants.Email.getName()));
	}

	public Applicant saveApplicant(ApplicantRequest applicant) {
		log.info("Applicant save request received.");
		var optionalApplicant = applicantMapper.getApplicant(applicant);
		return optionalApplicant.map(applicantRepository::save).orElseThrow(NotFoundException::new);
	}

	public Applicant updateApplicant(Applicant applicant) {
		log.info("Applicant update request received.");
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
			return new ResponseMessage(String.format("%s: %d %s", Constants.ApplicantID.getName(), applicantId, Constants.DeleteMessage.getName()));
		}
		throw new NotFoundException(Constants.ApplicantID.getName());
	}
}
