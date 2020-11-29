package com.careercenter.contoller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

import com.careercenter.model.Applicant;
import com.careercenter.model.ResponseMessage;
import com.careercenter.services.ApplicantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@Tag(name = "Applicant Controller", description = "Applicant API")
@ApiResponse(responseCode = "200", description = "Success")
@RequestMapping(value = "/applicant")
public class ApplicantController {

	@Autowired
	private ApplicantService applicantService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves All Applicants", description = "No need to pass parameters")
		@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Applicant>> retrieveAllApplicants() {
		return ResponseEntity.ok().body(applicantService.findAllApplicants());
	}

	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves an Applicant By Id", description = "Need to pass Applicant id")
	 @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Applicant> retrieveApplicantById(@PathVariable Long id) {
		return ResponseEntity.ok().body(applicantService.findApplicantById(id));
	}

	@GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves an Applicant By Email", description = "Need to pass Applicant email")
	 @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Applicant> retrieveApplicantByEmail(@PathVariable String email) {
		return ResponseEntity.ok().body(applicantService.findApplicantByEmail(email));
	}

	@PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a New Applicant", description = "Create a new Applicant by passing Applicant request")
	// @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Applicant> createApplicant(@Valid @NotNull @RequestBody Applicant applicant) {
		return ResponseEntity.ok().body(applicantService.saveApplicant(applicant));
	}

	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update Applicant Data", description = "Applicant Id must be in request.")
	// @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Applicant> updateApplicant(@Valid @NotNull @RequestBody Applicant Applicant) {
		return ResponseEntity.ok().body(applicantService.saveApplicant(Applicant));
	}

	@DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Delete an Applicant By Id", description = "Need to pass Applicant id")
	// @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<ResponseMessage> deleteApplicant(@Valid @NotNull @PathVariable long id) {
		return ResponseEntity.ok().body(applicantService.deleteApplicant(id));
	}

}
