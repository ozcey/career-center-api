package com.careercenter.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.careercenter.entities.Address;
import com.careercenter.entities.Company;
import com.careercenter.entities.Volunteer;
import com.careercenter.model.*;
import com.careercenter.repositories.CompanyRepository;
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
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    private static List<Company> companyList;

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

    public VolunteerResponse findVolunteerAndCompany(Long id) {
        return volunteerRepository.findVolunteerById(id).map(volunteer -> {
            List<Company> companies = companyRepository.findByVolunteerId(id);
            return VolunteerResponse.builder().volunteer(volunteer).companies(companies).build();
        }).orElseThrow(NotFoundException::new);
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
            Volunteer volunteer = getVolunteer(volunteerRequest);
            return volunteerRepository.save(volunteer);
        }
        throw new NotFoundException();
    }

    public VolunteerResponse updateVolunteer(Long volunteerId, SaveVolunteerRequest saveVolunteerRequest){
        log.info("Volunteer and Company update request received.");
        Volunteer volunteer = saveVolunteer(volunteerId,saveVolunteerRequest.getVolunteer());
        List<Company> cmpnylist = companyService.findCompanyByVolunteerId(volunteerId);
        List<CompanyRequest> companies = saveVolunteerRequest.getCompanies();
        cmpnylist.forEach(c -> companyList =  companies.stream().map(company -> companyService.updateCompany(c.getId(), company)).collect(Collectors.toList()));
        return VolunteerResponse.builder()
                .volunteer(volunteer)
                .companies(companyList)
                .build();
    }

    public Volunteer saveVolunteer(Long volunteerId, VolunteerRequest volunteerRequest) {
        log.info("Volunteer update request received.");
        return volunteerRepository.findVolunteerById(volunteerId).map(volunteer -> {
            var updatedVolunteer = getUpdatedVolunteer(volunteerRequest, volunteer);
            return volunteerRepository.save(updatedVolunteer);
        }).orElseThrow(NotFoundException::new);
    }

    public ResponseMessage deleteVolunteer(Long volunteerId) {
        log.info("Volunteer delete request received.");
        if (volunteerRepository.existsById(volunteerId)) {
            volunteerRepository.deleteById(volunteerId);
            return new ResponseMessage(String.format("%s: %d %s", Constants.VolunteerID.getName(), volunteerId, Constants.DeleteMessage.getName()));
        }
        throw new NotFoundException(Constants.VolunteerID.getName());
    }

    private Volunteer getUpdatedVolunteer(VolunteerRequest volunteerRequest, Volunteer volunteer){
        Volunteer savedVolunteer = getVolunteer(volunteerRequest);
        savedVolunteer.setId(volunteer.getId());
        savedVolunteer.getAddress().setId(volunteer.getAddress().getId());
        return savedVolunteer;
    }

    private Volunteer getVolunteer(VolunteerRequest volunteerRequest){
        return Volunteer.builder()
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
    }

}
