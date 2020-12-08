package com.careercenter.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.careercenter.entities.Address;
import com.careercenter.entities.Company;
import com.careercenter.entities.Volunteer;
import com.careercenter.model.*;
import com.careercenter.repositories.CompanyRepository;
import com.careercenter.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.careercenter.exception.NotFoundException;
import com.careercenter.repositories.VolunteerRepository;

@Slf4j
@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    public List<Volunteer> findAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Volunteer findVolunteerById(Long volunteerId) {
        return volunteerRepository.findVolunteerById(volunteerId)
                .orElseThrow(() -> new NotFoundException(Utils.VolunteerID.getName()));
    }

    public Volunteer findVolunteerByEmail(String email) {
        return volunteerRepository.findVolunteerByEmail(email).orElseThrow(() -> new NotFoundException(Utils.Email.getName()));
    }

    public VolunteerResponse findVolunteerAndCompany(Long id) {
        Volunteer volunteer = findVolunteerById(id);
        if (volunteer != null) {
            List<Company> companies = companyRepository.findByVolunteerId(id);
            return VolunteerResponse.builder().volunteer(volunteer).companies(companies).build();
        }
        throw new NotFoundException();
    }

    public VolunteerResponse createVolunteer(SaveVolunteerRequest saveVolunteerRequest){
        log.info("Volunteer create request received.");
        Volunteer volunteer = saveVolunteer(saveVolunteerRequest.getVolunteer());
        var id = volunteer.getId();
        List<CompanyRequest> companies = saveVolunteerRequest.getCompanies();
        List<Company> companyList = companies.stream().map(company -> companyService.saveCompany(id, company)).collect(Collectors.toList());
        return VolunteerResponse.builder()
                .volunteer(volunteer)
                .companies(companyList)
                .build();
    }

    private Volunteer saveVolunteer(VolunteerRequest volunteerRequest){
        log.info("Volunteer save request received.");
        Optional<VolunteerRequest> vRequest = Optional.ofNullable(volunteerRequest);
        if(vRequest.isPresent()){
            Volunteer volunteer = Volunteer.builder()
                    .firstName(volunteerRequest.getFirstName())
                    .lastName(volunteerRequest.getLastName())
                    .email(volunteerRequest.getEmail())
                    .phone(volunteerRequest.getPhone())
                    .jobTitle(volunteerRequest.getJobTitle())
                    .industry(volunteerRequest.getIndustry())
                    .otherIndustries(volunteerRequest.getOtherIndustries())
                    .yearsOfExperience(volunteerRequest.getYearsOfExperience())
                    .languages(volunteerRequest.getLanguages())
                    .address(Address.builder()
                            .street(volunteerRequest.getAddress().getStreet())
                            .city(volunteerRequest.getAddress().getCity())
                            .state(volunteerRequest.getAddress().getState())
                            .zipcode(volunteerRequest.getAddress().getZipcode())
                            .build())
                    .build();
            return volunteerRepository.save(volunteer);
        }
        throw new NotFoundException();
    }

    public Volunteer updateVolunteer(Volunteer volunteer) {
        log.info("Volunteer update request received.");
        Optional<Volunteer> optionalVolunteer = Optional.ofNullable(volunteer);
        if (optionalVolunteer.isPresent()) {
            return volunteerRepository.save(volunteer);
        }
        throw new NotFoundException();
    }

    public ResponseMessage deleteVolunteer(Long volunteerId) {
        log.info("Volunteer delete request received.");
        if (volunteerRepository.existsById(volunteerId)) {
            volunteerRepository.deleteById(volunteerId);
            return new ResponseMessage(String.format("%s %s", Utils.VolunteerID.getName(), Utils.DeleteMessage.getName()));
        }
        throw new NotFoundException(Utils.VolunteerID.getName());
    }

}
