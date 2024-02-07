package com.careercenter.mapper;

import com.careercenter.entities.Address;
import com.careercenter.entities.Volunteer;
import com.careercenter.model.VolunteerRequest;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class VolunteerMapper {

    public Optional<Volunteer> getVolunteer(VolunteerRequest volunteerRequest){
        if(volunteerRequest != null){
            Volunteer volunteer = Volunteer.builder()
                    .name(volunteerRequest.getName())
                    .email(volunteerRequest.getEmail())
                    .phone(volunteerRequest.getPhone())
                    .jobTitle(volunteerRequest.getJobTitle())
                    .industry(volunteerRequest.getIndustry())
                    .areaOfInterest(volunteerRequest.getAreaOfInterest())
                    .build();
            return Optional.of(volunteer);
        }
        return Optional.empty();
    }

}
