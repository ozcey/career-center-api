package com.careercenter.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.careercenter.model.*;
import com.careercenter.repositories.CompanyRepository;
import com.careercenter.repositories.OtherIndustryRepository;
import com.careercenter.repositories.VLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.careercenter.exception.NotFoundException;
import com.careercenter.repositories.VolunteerRepository;

@Service
public class VolunteerService {
//    TODO: update volunteer request(already done) and response, then try to create save and update

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private VLanguageRepository vLanguageRepository;

    @Autowired
    private OtherIndustryRepository otherIndustryRepository;

    public List<Volunteer> findAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Volunteer findVolunteerById(Long volunteerId) {
        return volunteerRepository.findVolunteerById(volunteerId)
                .orElseThrow(() -> new NotFoundException("volunteerId"));
    }

    public Volunteer findVolunteerByEmail(String email) {
        return volunteerRepository.findVolunteerByEmail(email).orElseThrow(() -> new NotFoundException("email"));
    }

    public VolunteerResponse findVolunteerAndCompany(Long id) {
        Volunteer volunteer = findVolunteerById(id);
        if (volunteer != null) {
            List<Company> companies = companyRepository.findByVolunteerId(id);
            return VolunteerResponse.builder().volunteer(volunteer).companies(companies).build();
        }
        throw new NotFoundException();
    }

    public Volunteer updateVolunteer(Volunteer volunteer) {
        Optional<Volunteer> optionalVolunteer = Optional.ofNullable(volunteer);
        if (optionalVolunteer.isPresent()) {
            return volunteerRepository.save(volunteer);
        }
        throw new NotFoundException();
    }

    public VolunteerResponse createVolunteer(VolunteerRequest volunteerRequest){
        Volunteer volunteer = saveVolunteer(volunteerRequest);
        var id = volunteer.getId();
        List<OtherIndustry> otherIndustries = saveOtherIndustry(id, volunteerRequest.getOtherIndustry());
        Company company = saveCompany(id, volunteerRequest.getCompany());
        List<VolunteerLanguage> vLanguages = saveVLanguage(id, volunteerRequest.getLanguages());
        return VolunteerResponse.builder()
                .volunteer(volunteer)
                .otherIndustries(otherIndustries)
                .companies(Arrays.asList(company))
                .languages(vLanguages)
                .build();
    }

    private Volunteer saveVolunteer(VolunteerRequest volunteerRequest){
        Optional<VolunteerRequest> vRequest = Optional.ofNullable(volunteerRequest);
        if(vRequest.isPresent()){
            Volunteer volunteer = Volunteer.builder()
                    .firstName(volunteerRequest.getFirstName())
                    .lastName(volunteerRequest.getLastName())
                    .email(volunteerRequest.getEmail())
                    .phone(volunteerRequest.getPhone())
                    .jobTitle(volunteerRequest.getJobTitle())
                    .industry(volunteerRequest.getIndustry())
                    .yearsOfExperience(volunteerRequest.getYearsOfExperience())
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

    private List<OtherIndustry> saveOtherIndustry(Long volunteerId, List<String> otherIndustries){
        if(volunteerRepository.existsById(volunteerId)){
            return volunteerRepository.findVolunteerById(volunteerId).map(volunteer -> {
               otherIndustries.forEach(industry -> {
                   OtherIndustry otherIndustry = new OtherIndustry();
                   otherIndustry.setIndustry(industry);
                   otherIndustry.setVolunteer(volunteer);
                   otherIndustryRepository.save(otherIndustry);
               });
               return otherIndustryRepository.findIndustryByVolunteerId(volunteer.getId());
            }).orElseThrow(NotFoundException::new);
        }
        throw new NotFoundException();
    }

    private List<VolunteerLanguage> saveVLanguage(Long volunteerId, List<String> languages){
        if(volunteerRepository.existsById(volunteerId)){
            return volunteerRepository.findVolunteerById(volunteerId).map(volunteer -> {
                languages.forEach(language -> {
                    VolunteerLanguage vLanguage = new VolunteerLanguage();
                    vLanguage.setLanguage(language);
                    vLanguage.setVolunteer(volunteer);
                   vLanguageRepository.save(vLanguage);
                });
                return vLanguageRepository.findVLanguageByVolunteerId(volunteer.getId());
            }).orElseThrow(NotFoundException::new);
        }
        throw new NotFoundException();
    }

    public Company saveCompany(Long volunteerId, Company company){
        if(volunteerRepository.existsById(volunteerId)){
            return volunteerRepository.findVolunteerById(volunteerId).map(volunteer -> {
                Company savedCompany = Company.builder()
                        .name(company.getName())
                        .city(company.getCity())
                        .state(company.getState())
                        .volunteer(volunteer)
                        .build();
                return companyRepository.save(savedCompany);
            }).orElseThrow(NotFoundException::new);
        }
        throw new NotFoundException();
    }

    public ResponseMessage deleteVolunteer(Long id) {
        if (volunteerRepository.existsById(id)) {
            volunteerRepository.deleteById(id);
            return new ResponseMessage("Volunteer deleted successfully.");
        }
        throw new NotFoundException("id");
    }

}
