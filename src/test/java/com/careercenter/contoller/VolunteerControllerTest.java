package com.careercenter.contoller;

import com.careercenter.entities.Applicant;
import com.careercenter.entities.Volunteer;
import com.careercenter.integration.IntegrationTestData;
import com.careercenter.model.ApplicantRequest;
import com.careercenter.model.ResponseMessage;
import com.careercenter.model.VolunteerRequest;
import com.careercenter.services.ApplicantService;
import com.careercenter.services.VolunteerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

@WebMvcTest(controllers = VolunteerController.class)
@WithMockUser(username = "admin", password = "password", roles = "ADMIN")
class VolunteerControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private VolunteerService volunteerService;
    @Autowired
    private ObjectMapper objectMapper;
    private List<Volunteer> volunteerList;
    private VolunteerRequest volunteerRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        volunteerList = IntegrationTestData.setVolunteerist();
        volunteerRequest = IntegrationTestData.getVolunteerRequest();
    }

    @Test
    void retrieveAllVolunteer() throws Exception {
        given(volunteerService.findAllVolunteers()).willReturn(volunteerList);

        ResultActions response = mockMvc.perform(get("/volunteer"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(volunteerList.size())));
    }

    @Test
    void retrieveVolunteerById() throws Exception {
        Volunteer volunteer = volunteerList.get(0);
        volunteer.setId(1L);
        given(volunteerService.findVolunteerById(volunteer.getId())).willReturn(volunteer);

        ResultActions response = mockMvc.perform(get("/volunteer/id/{id}", volunteer.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(volunteer.getName())))
                .andExpect(jsonPath("$.phone", is(volunteer.getPhone())))
                .andExpect(jsonPath("$.email", is(volunteer.getEmail())))
                .andDo(print());
    }

    @Test
    void retrieveVolunteerByEmail() throws Exception {
    	  Volunteer volunteer = volunteerList.get(0);

        given(volunteerService.findVolunteerByEmail(volunteer.getEmail())).willReturn(volunteer);

        ResultActions response = mockMvc.perform(get("/volunteer/email/{email}", volunteer.getEmail()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(volunteer.getName())))
                .andExpect(jsonPath("$.phone", is(volunteer.getPhone())))
                .andExpect(jsonPath("$.email", is(volunteer.getEmail())))
                .andDo(print());
    }

    @Test
    void createVolunteer() throws Exception {
    	 Volunteer volunteer = volunteerList.get(0);

        willReturn(volunteer).given(volunteerService).saveVolunteer(volunteerRequest);

        ResultActions response = mockMvc.perform(post("/volunteer/save")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(volunteerRequest))
        );

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(volunteer.getName())))
                .andExpect(jsonPath("$.phone", is(volunteer.getPhone())))
                .andExpect(jsonPath("$.email", is(volunteer.getEmail())))
                .andDo(print());
    }

    @Test
    void updateVolunteer() throws Exception {
    	 Volunteer volunteer = volunteerList.get(0);
    	 volunteer.setId(1L);
        willReturn(volunteer).given(volunteerService).updateVolunteer(volunteer);

        volunteer.setName("Andy Joe");
        volunteer.setPhone("1112345678");

        ResultActions response = mockMvc.perform(put("/volunteer/update")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(volunteer))
        );

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(volunteer.getName())))
                .andExpect(jsonPath("$.phone", is(volunteer.getPhone())))
                .andDo(print());
    }

    @Test
    void deleteApplicant() throws Exception {
        long applicantId = 1L;
        ResponseMessage message = new ResponseMessage(String.format("Volunteer with id: %s deleted successfully.", applicantId));

        given(volunteerService.deleteVolunteer(applicantId)).willReturn(message);

        ResultActions response = mockMvc.perform(delete("/volunteer/delete/{id}", applicantId));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("Volunteer with id: %s deleted successfully.", applicantId))))
                .andDo(print());
    }
}