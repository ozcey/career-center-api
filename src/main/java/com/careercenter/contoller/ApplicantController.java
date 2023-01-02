package com.careercenter.contoller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.careercenter.model.ApplicantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careercenter.entities.Applicant;
import com.careercenter.model.ResponseMessage;
import com.careercenter.services.ApplicantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@Tag(name = "Applicant Controller", description = "Applicant API")
@ApiResponse(responseCode = "200", description = "Success")
@RequestMapping(value = "/applicant")
@RequiredArgsConstructor
public class ApplicantController {

	private final ApplicantService applicantService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves All Applicants", description = "No need to pass parameters")
	public ResponseEntity<List<Applicant>> retrieveAllApplicants() {
		return ResponseEntity.ok().body(applicantService.findAllApplicants());
	}

	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves an Applicant By Id", description = "Need to pass Applicant id")
	public ResponseEntity<Applicant> retrieveApplicantById(@PathVariable Long id) {
		return ResponseEntity.ok().body(applicantService.findApplicantById(id));
	}

	@GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves an Applicant By Email", description = "Need to pass Applicant email")
	public ResponseEntity<Applicant> retrieveApplicantByEmail(@PathVariable String email) {
		return ResponseEntity.ok().body(applicantService.findApplicantByEmail(email));
	}

	@PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a New Applicant", description = "Create a new Applicant by passing Applicant request")
	public ResponseEntity<Applicant> createApplicant(@Valid @NotNull @RequestBody ApplicantRequest applicant) {
		return ResponseEntity.ok().body(applicantService.saveApplicant(applicant));
	}

	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update Applicant Data", description = "Applicant Id must be in request.")
	public ResponseEntity<Applicant> updateApplicant(@Valid @NotNull @RequestBody Applicant Applicant) {
		return ResponseEntity.ok().body(applicantService.updateApplicant(Applicant));
	}

	@DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Delete an Applicant By Id", description = "Need to pass Applicant id")
	public ResponseEntity<ResponseMessage> deleteApplicant(@Valid @NotNull @PathVariable long id) {
		return ResponseEntity.ok().body(applicantService.deleteApplicant(id));
	}

}
