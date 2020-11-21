package com.careercenter.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.careercenter.exception.NotFoundException;
import com.careercenter.model.Volunteer;
import com.careercenter.model.ResponseMessage;
import com.careercenter.repositories.VolunteerRepository;

@Service
public class VolunteerService {

	private static final String message = "Volunteer deleted successfully.";

	@Autowired
	private VolunteerRepository volunteerRepository;

	public List<Volunteer> findAllVolunteers() {
		return volunteerRepository.findAll();
	}

	public Volunteer findVolunteerById(Long volunteer_id) {
		return volunteerRepository.findVolunteerById(volunteer_id)
				.orElseThrow(() -> new NotFoundException("volunteer_id"));
	}

	public Volunteer findVolunteerByEmail(String email) {
		return volunteerRepository.findVolunteerByEmail(email).orElseThrow(() -> new NotFoundException("email"));
	}

	public Volunteer saveVolunteer(Volunteer volunteer) {
		Optional<Volunteer> optionalVolunteer = Optional.ofNullable(volunteer);
		if (optionalVolunteer.isPresent()) {
			return volunteerRepository.save(volunteer);
		}
		throw new NotFoundException();
	}

	public ResponseMessage deleteVolunteer(Long id) {
		if (volunteerRepository.existsById(id)) {
			volunteerRepository.deleteById(id);
			return new ResponseMessage(message);
		}
		throw new NotFoundException("id");
	}

}
