package com.careercenter.mapper;

import com.careercenter.entities.Address;
import com.careercenter.entities.Applicant;
import com.careercenter.model.ApplicantRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicantMapper {

    public Optional<Applicant> getApplicant(ApplicantRequest applicantRequest){
        if (applicantRequest != null){
            Applicant applicant = Applicant.builder()
                    .firstName(applicantRequest.getFirstName())
                    .lastName(applicantRequest.getLastName())
                    .email(applicantRequest.getEmail())
                    .phone(applicantRequest.getPhone())
                    .age(applicantRequest.getAge())
                    .category(applicantRequest.getCategory())
                    .degree(applicantRequest.getDegree())
                    .gender(applicantRequest.getGender())
                    .languages(applicantRequest.getLanguages())
                    .address(Address.builder()
                            .street(applicantRequest.getAddress().getStreet())
                            .city(applicantRequest.getAddress().getCity())
                            .state(applicantRequest.getAddress().getState())
                            .zipcode(applicantRequest.getAddress().getZipcode())
                            .build())
                    .build();
            return Optional.of(applicant);
        }
        return Optional.empty();
    }
}
