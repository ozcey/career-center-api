package com.careercenter.services;

import com.careercenter.entities.Applicant;
import com.careercenter.exception.NotFoundException;
import com.careercenter.integration.IntegrationTestData;
import com.careercenter.mapper.ApplicantMapper;
import com.careercenter.model.ApplicantRequest;
import com.careercenter.model.ResponseMessage;
import com.careercenter.repositories.ApplicantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class ApplicantServiceTest {
    @Mock
    private ApplicantRepository applicantRepository;
    @InjectMocks
    private ApplicantService applicantService;
    @Mock
    private ApplicantMapper applicantMapper;
    private List<Applicant> applicantList;
    private ApplicantRequest applicantRequest;

    @BeforeEach
    void setUp() {
        applicantList = IntegrationTestData.setApplicantList();
        applicantRequest = IntegrationTestData.getApplicantRequest();
    }

    @Test
    void findAllApplicants() {
        given(applicantRepository.findAll()).willReturn(applicantList);
        List<Applicant> applicants = applicantService.findAllApplicants();
        assertThat(applicants).isNotEmpty();
        assertThat(applicants).hasSize(applicantList.size());
    }

    @Test
    void findApplicantById() {
        Applicant applicant = applicantList.get(0);
        given(applicantRepository.findApplicantById(applicant.getId())).willReturn(Optional.of(applicant));
        Applicant returnedApplicant = applicantService.findApplicantById(applicant.getId());

        assertThat(returnedApplicant).isNotNull();
        assertThat(returnedApplicant.getFirstName()).isEqualTo(applicant.getFirstName());
        assertThat(returnedApplicant.getLastName()).isEqualTo(applicant.getLastName());
        assertThat(returnedApplicant.getEmail()).isEqualTo(applicant.getEmail());
    }

    @Test
    void findApplicantByIdThrowsNotFound() {
        Applicant applicant = applicantList.get(0);
        given(applicantRepository.findApplicantById(applicant.getId())).willThrow(new NotFoundException());

        Assertions.assertThrows(NotFoundException.class, () -> {
            applicantService.findApplicantById(applicant.getId());
        });
        verify(applicantRepository, times(1)).findApplicantById(applicant.getId());
    }

    @Test
    void findApplicantByEmail() {
        Applicant applicant = applicantList.get(0);
        given(applicantRepository.findApplicantByEmail(applicant.getEmail())).willReturn(Optional.of(applicant));
        Applicant returnedApplicant = applicantService.findApplicantByEmail(applicant.getEmail());

        assertThat(returnedApplicant).isNotNull();
        assertThat(returnedApplicant.getFirstName()).isEqualTo(applicant.getFirstName());
        assertThat(returnedApplicant.getLastName()).isEqualTo(applicant.getLastName());
        assertThat(returnedApplicant.getEmail()).isEqualTo(applicant.getEmail());
    }

    @Test
    void findApplicantByEmailThrowsNotFound() {
        Applicant applicant = applicantList.get(0);
        given(applicantRepository.findApplicantByEmail(applicant.getEmail())).willThrow(new NotFoundException());

        Assertions.assertThrows(NotFoundException.class, () -> {
            applicantService.findApplicantByEmail(applicant.getEmail());
        });
        verify(applicantRepository, times(1)).findApplicantByEmail(applicant.getEmail());
    }

    @Test
    void saveApplicant() {
        Applicant applicant = applicantList.get(0);
        given(applicantRepository.save(applicant)).willReturn(applicant);
        given(applicantMapper.getApplicant(applicantRequest)).willReturn(Optional.of(applicant));
        Applicant savedApplicant = applicantService.saveApplicant(applicantRequest);

        assertThat(savedApplicant).isNotNull();
        assertThat(savedApplicant.getFirstName()).isEqualTo(applicant.getFirstName());
        assertThat(savedApplicant.getLastName()).isEqualTo(applicant.getLastName());
        assertThat(savedApplicant.getEmail()).isEqualTo(applicant.getEmail());
    }

    @Test
    void updateApplicant() {
        Applicant applicant = applicantList.get(0);
        applicant.setId(1L);

        Applicant updatedApplicant = applicantList.get(1);
        updatedApplicant.setId(applicant.getId());

        given(applicantRepository.save(applicant)).willReturn(updatedApplicant);
        Applicant savedApplicant = applicantService.updateApplicant(applicant);

        assertThat(savedApplicant).isNotNull();
        assertThat(savedApplicant.getFirstName()).isEqualTo(updatedApplicant.getFirstName());
        assertThat(savedApplicant.getLastName()).isEqualTo(updatedApplicant.getLastName());
        assertThat(savedApplicant.getEmail()).isEqualTo(updatedApplicant.getEmail());
    }

    @Test
    void deleteApplicant() {
        Applicant applicant = applicantList.get(0);
        applicant.setId(1L);

        given(applicantRepository.existsById(applicant.getId())).willReturn(true);
        willDoNothing().given(applicantRepository).deleteById(applicant.getId());

        ResponseMessage message = applicantService.deleteApplicant(applicant.getId());

        assertThat(message).isNotNull();
        assertThat(message.getMessage()).isEqualTo(String.format("Applicant with id: %s deleted successfully.", applicant.getId()));
    }
}