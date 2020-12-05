package com.careercenter.services;

import com.careercenter.exception.NotFoundException;
import com.careercenter.model.Company;
import com.careercenter.model.ResponseMessage;
import com.careercenter.repositories.CompanyRepository;
import com.careercenter.repositories.VolunteerRepository;
import com.careercenter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<Company> findCompanyByVolunteerId(Long id) {
        return companyRepository.findByVolunteerId(id);
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

    public Company updateCompany(Long volunteerId, Company company){
        if(volunteerRepository.existsById(volunteerId)){
            return companyRepository.findById(company.getId()).map(updatedCompany -> {
                updatedCompany.setName(company.getName());
                updatedCompany.setCity(company.getCity());
                updatedCompany.setState(company.getState());
                return companyRepository.save(updatedCompany);
            }).orElseThrow(NotFoundException::new);
        }
        throw new NotFoundException();
    }

    public ResponseMessage deleteCompanyById(Long companyId){
        if (companyRepository.existsById(companyId)){
            companyRepository.deleteById(companyId);
            return new ResponseMessage(String.format("%s %s", Utils.CompanyID.getName(), Utils.DeleteMessage.getName()));
        }
        throw new NotFoundException(Utils.CompanyID.getName());
    }
}
