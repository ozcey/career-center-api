package com.careercenter.integration;

import com.careercenter.entities.Address;
import com.careercenter.entities.Applicant;
import com.careercenter.repositories.ApplicantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Arrays;
import java.util.List;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApplicantControllerIntegrationTests extends AbstractContainerBaseTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private List<Applicant> applicantList;

    @BeforeEach
    void setUp() {
        applicantRepository.deleteAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        applicantList = IntegrationTestData.setApplicantList();
    }

    @Test
    void retrieveAllApplicants() throws Exception {
        applicantRepository.saveAll(applicantList);

        ResultActions response = mockMvc.perform(get("/applicant")
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(applicantList.size())))
                .andDo(print());
    }

    @Test
    void retrieveApplicantById() throws Exception {
        Applicant savedApplicant = applicantRepository.save(applicantList.get(0));

        ResultActions response = mockMvc.perform(get("/applicant/id/{id}", savedApplicant.getId())
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(savedApplicant.getEmail())))
                .andExpect(jsonPath("$.firstName", is(savedApplicant.getFirstName())))
                .andDo(print());
    }

    @Test
    void retrieveApplicantByEmail() throws Exception{
        Applicant savedApplicant = applicantRepository.save(applicantList.get(0));

        ResultActions response = mockMvc.perform(get("/applicant/email/{email}", savedApplicant.getEmail())
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(savedApplicant.getEmail())))
                .andExpect(jsonPath("$.firstName", is(savedApplicant.getFirstName())))
                .andDo(print());
    }

    @Test
    void createApplicant() throws Exception {
        Applicant applicant = applicantList.get(0);
        ResultActions response = mockMvc.perform(post("/applicant/save")
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicant))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(applicant.getEmail())))
                .andExpect(jsonPath("$.firstName", is(applicant.getFirstName())))
                .andExpect(jsonPath("$.phone", is(applicant.getPhone())))
                .andDo(print());
    }

    @Test
    void updateApplicant() throws Exception {
        Applicant savedApplicant = applicantRepository.save(applicantList.get(0));
        savedApplicant.setFirstName("Mike");
        savedApplicant.setLastName("Smith");
        savedApplicant.setPhone("3456789012");

        ResultActions response = mockMvc.perform(put("/applicant/update")
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedApplicant))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(savedApplicant.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(savedApplicant.getLastName())))
                .andExpect(jsonPath("$.phone", is(savedApplicant.getPhone())))
                .andDo(print());
    }

    @Test
    void deleteApplicant() throws Exception {
        Applicant savedApplicant = applicantRepository.save(applicantList.get(0));

        ResultActions response = mockMvc.perform(delete("/applicant/delete/{id}", savedApplicant.getId())
                .with(user("admin"))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("Applicant with id: %s deleted successfully.", savedApplicant.getId()))))
                .andDo(print());

    }
}