package com.careercenter.contoller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.careercenter.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careercenter.services.VolunteerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@Tag(name = "Volunteer Controller", description = "Volunteer API")
@ApiResponse(responseCode = "200", description = "Success")
@RequestMapping(value = "/volunteer")
public class VolunteerController {

	@Autowired
	private VolunteerService volunteerService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves All Volunteers", description = "No need to pass parameters")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Volunteer>> retrieveAllVolunteers() {
		return ResponseEntity.ok().body(volunteerService.findAllVolunteers());
	}

	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves an Volunteer By Id", description = "Need to pass Volunteer Id")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Volunteer> retrieveVolunteerById(@PathVariable Long id) {
		return ResponseEntity.ok().body(volunteerService.findVolunteerById(id));
	}

	@GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves an Volunteer By Email", description = "Need to pass Volunteer email")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Volunteer> retrieveVolunteerByEmail(@PathVariable String email) {
		return ResponseEntity.ok().body(volunteerService.findVolunteerByEmail(email));
	}

	@GetMapping(value = "/{volunteerId}/company", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves volunteer and company by volunteer id", description = "Need to pass volunteer id")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<VolunteerResponse> retrieveVolunteerAndCompany(@PathVariable Long volunteerId){
		return ResponseEntity.ok().body(volunteerService.findVolunteerAndCompany(volunteerId));
	}

	@PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a New Volunteer", description = "Create a new Volunteer by passing Volunteer request")
	public ResponseEntity<VolunteerResponse> createVolunteer(@Valid @NotNull @RequestBody VolunteerRequest volunteer) {
		return ResponseEntity.ok().body(volunteerService.createVolunteer(volunteer));
	}

	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update Volunteer Data", description = "Volunteer Id must be in request.")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Volunteer> updateVolunteer(@Valid @NotNull @RequestBody Volunteer volunteer) {
		return ResponseEntity.ok().body(volunteerService.updateVolunteer(volunteer));
	}

	@DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Delete an Volunteer By Id", description = "Need to pass Volunteer id")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<ResponseMessage> deleteVolunteer(@Valid @NotNull @PathVariable long id) {
		return ResponseEntity.ok().body(volunteerService.deleteVolunteer(id));
	}

}
