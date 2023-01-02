package com.careercenter.contoller;

import com.careercenter.entities.Applicant;
import com.careercenter.integration.IntegrationTestData;
import com.careercenter.model.ApplicantRequest;
import com.careercenter.model.ResponseMessage;
import com.careercenter.services.ApplicantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = ApplicantController.class)
class ApplicantControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private ApplicantService applicantService;
    @Autowired
    private ObjectMapper objectMapper;
    private List<Applicant> applicantList;
    private ApplicantRequest applicantRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        applicantList = IntegrationTestData.setApplicantList();
        applicantRequest = IntegrationTestData.getApplicantRequest();
    }

    @Test
    void retrieveAllApplicants() throws Exception {
        given(applicantService.findAllApplicants()).willReturn(applicantList);

        ResultActions response = mockMvc.perform(get("/applicant"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(applicantList.size())));
    }

    @Test
    void retrieveApplicantById() throws Exception {
        Applicant applicant = applicantList.get(0);
        applicant.setId(1L);
        given(applicantService.findApplicantById(applicant.getId())).willReturn(applicant);

        ResultActions response = mockMvc.perform(get("/applicant/id/{id}", applicant.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(applicant.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(applicant.getLastName())))
                .andExpect(jsonPath("$.email", is(applicant.getEmail())))
                .andDo(print());
    }

    @Test
    void retrieveApplicantByEmail() throws Exception {
        Applicant applicant = applicantList.get(0);

        given(applicantService.findApplicantByEmail(applicant.getEmail())).willReturn(applicant);

        ResultActions response = mockMvc.perform(get("/applicant/email/{email}", applicant.getEmail()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(applicant.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(applicant.getLastName())))
                .andExpect(jsonPath("$.email", is(applicant.getEmail())))
                .andDo(print());
    }

    @Test
    void createApplicant() throws Exception {
        Applicant applicant = applicantList.get(0);

        willReturn(applicant).given(applicantService).saveApplicant(applicantRequest);

        ResultActions response = mockMvc.perform(post("/applicant/save")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicantRequest))
        );

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(applicant.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(applicant.getLastName())))
                .andExpect(jsonPath("$.email", is(applicant.getEmail())))
                .andDo(print());
    }

    @Test
    void updateApplicant() throws Exception {
        Applicant applicant = applicantList.get(0);

        willReturn(applicant).given(applicantService).updateApplicant(applicant);

        applicant.setFirstName("Mark");
        applicant.setLastName("Smith");

        ResultActions response = mockMvc.perform(put("/applicant/update")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(applicant))
        );

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(applicant.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(applicant.getLastName())))
                .andDo(print());
    }

    @Test
    void deleteApplicant() throws Exception {
        long applicantId = 1L;
        ResponseMessage message = new ResponseMessage(String.format("Applicant with id: %s deleted successfully.", applicantId));

        given(applicantService.deleteApplicant(applicantId)).willReturn(message);

        ResultActions response = mockMvc.perform(delete("/applicant/delete/{id}", applicantId));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("Applicant with id: %s deleted successfully.", applicantId))))
                .andDo(print());
    }
}