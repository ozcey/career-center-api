package com.careercenter.services;

import java.util.List;
import java.util.Optional;

import com.careercenter.entities.Address;
import com.careercenter.model.ApplicantRequest;
import com.careercenter.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.careercenter.exception.NotFoundException;
import com.careercenter.entities.Applicant;
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
				.orElseThrow(() -> new NotFoundException(Constants.ApplicantID.getName()));
	}

	public Applicant findApplicantByEmail(String email) {
		return applicantRepository.findApplicantByEmail(email).orElseThrow(() -> new NotFoundException(Constants.Email.getName()));
	}

	public Applicant saveApplicant(ApplicantRequest applicant) {
		log.info("Applicant save request received.");
		Optional<ApplicantRequest> optionalApplicant = Optional.ofNullable(applicant);
		if (optionalApplicant.isPresent()) {
			Applicant savedApplicant = Applicant.builder()
					.firstName(applicant.getFirstName())
					.lastName(applicant.getLastName())
					.email(applicant.getEmail())
					.phone(applicant.getPhone())
					.age(applicant.getAge())
					.category(applicant.getCategory())
					.degree(applicant.getDegree())
					.gender(applicant.getGender())
					.languages(applicant.getLanguages())
					.address(Address.builder()
							.street(applicant.getAddress().getStreet())
							.city(applicant.getAddress().getCity())
							.state(applicant.getAddress().getState())
							.zipcode(applicant.getAddress().getZipcode())
							.build())
					.build();
			return applicantRepository.save(savedApplicant);
		}
		throw new NotFoundException();
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
			return new ResponseMessage(String.format("%s %s", Constants.ApplicantID.getName(), Constants.DeleteMessage.getName()));

		}
		throw new NotFoundException(Constants.ApplicantID.getName());
	}

}
