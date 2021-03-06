package com.careercenter.services;

import com.careercenter.exception.NotFoundException;
import com.careercenter.entities.Company;
import com.careercenter.mapper.VolunteerMapper;
import com.careercenter.model.CompanyRequest;
import com.careercenter.model.ResponseMessage;
import com.careercenter.repositories.CompanyRepository;
import com.careercenter.repositories.VolunteerRepository;
import com.careercenter.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final VolunteerRepository volunteerRepository;
    private final VolunteerMapper volunteerMapper;

    public List<Company> findCompanyByVolunteerId(Long id) {
        return companyRepository.findByVolunteerId(id);
    }

    public Company saveCompany(Long volunteerId, CompanyRequest company){
        log.info("Company save request received.");
        if(volunteerRepository.existsById(volunteerId)){
            return volunteerRepository.findVolunteerById(volunteerId).map(volunteer -> {
                Company savedCompany = volunteerMapper.getCompany(company, volunteer);
                return companyRepository.save(savedCompany);
            }).orElseThrow(NotFoundException::new);
        }
        throw new NotFoundException();
    }

    public Company updateCompany(Long companyId, CompanyRequest company){
        log.info("Company update request received.");
        if(companyRepository.existsById(companyId)){
            return companyRepository.findById(companyId).map(updatedCompany -> {
                updatedCompany.setName(company.getName());
                updatedCompany.setCity(company.getCity());
                updatedCompany.setState(company.getState());
                return companyRepository.save(updatedCompany);
            }).orElseThrow(NotFoundException::new);
        }
        throw new NotFoundException();
    }

    public ResponseMessage deleteCompanyById(Long companyId){
        log.info("Company delete request received.");
        if (companyRepository.existsById(companyId)){
            companyRepository.deleteById(companyId);
            return new ResponseMessage(String.format("%s %s", Constants.CompanyID.getName(), Constants.DeleteMessage.getName()));
        }
        throw new NotFoundException(Constants.CompanyID.getName());
    }
}
