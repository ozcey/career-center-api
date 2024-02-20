package com.careercenter.services;

import java.util.List;
import java.util.Optional;


import com.careercenter.entities.Volunteer;
import com.careercenter.mapper.VolunteerMapper;
import com.careercenter.model.*;
import com.careercenter.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.careercenter.exception.NotFoundException;
import com.careercenter.repositories.VolunteerRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerMapper volunteerMapper;

    public List<Volunteer> findAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Volunteer findVolunteerById(Long volunteerId) {
        return volunteerRepository.findVolunteerById(volunteerId)
                .orElseThrow(() -> new NotFoundException(Constants.VolunteerID.getName()));
    }

    public Volunteer findVolunteerByEmail(String email) {
        return volunteerRepository.findVolunteerByEmail(email).orElseThrow(() -> new NotFoundException(Constants.Email.getName()));
    }

    public Volunteer saveVolunteer(VolunteerRequest volunteerRequest){
        log.info("Volunteer save request received.");
        Optional<Volunteer> optionalVolunteer = volunteerMapper.getVolunteer(volunteerRequest);
        return optionalVolunteer.map(volunteerRepository::save).orElseThrow(NotFoundException::new);
    }

    public Volunteer updateVolunteer(Volunteer volunteer) {
        log.info("Volunteer update request received.");
        if(volunteerRepository.existsById(volunteer.getId())) {
        	return volunteerRepository.save(volunteer);
        }
        throw new NotFoundException();
    }

    public ResponseMessage deleteVolunteer(Long volunteerId) {
        log.info("Volunteer delete request received.");
        if (volunteerRepository.existsById(volunteerId)) {
            volunteerRepository.deleteById(volunteerId);
            return new ResponseMessage(String.format("%s with id: %d %s", "Volunteer", volunteerId, Constants.DeleteMessage.getName()));
        }
        throw new NotFoundException(Constants.VolunteerID.getName());
    }
}
