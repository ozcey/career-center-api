package com.careercenter.mapper;

import com.careercenter.entities.Address;
import com.careercenter.entities.Company;
import com.careercenter.entities.Volunteer;
import com.careercenter.model.CompanyRequest;
import com.careercenter.model.VolunteerRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class VolunteerMapper {

    public Optional<Volunteer> getVolunteer(VolunteerRequest volunteerRequest){
        if(volunteerRequest != null){
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
            return Optional.of(volunteer);
        }
        return Optional.empty();
    }

    public Optional<Volunteer> getUpdatedVolunteer(VolunteerRequest volunteerRequest, Volunteer volunteer){
       var volunteerOptional = getVolunteer(volunteerRequest);
       if(volunteerOptional.isPresent()){
           var savedVolunteer = volunteerOptional.get();
           savedVolunteer.setId(volunteer.getId());
           savedVolunteer.getAddress().setId(volunteer.getAddress().getId());
           return Optional.of(savedVolunteer);
       }
        return Optional.empty();
    }

    public Company getCompany(CompanyRequest company, Volunteer volunteer) {
        var savedCompany =  new Company();
        if(company != null || volunteer != null){
            savedCompany = Company.builder()
                    .name(company.getName())
                    .city(company.getCity())
                    .state(company.getState())
                    .volunteer(volunteer)
                    .build();
        }
        return savedCompany;
    }

}
