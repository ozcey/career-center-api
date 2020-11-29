package com.careercenter.contoller;

import com.careercenter.model.Company;
import com.careercenter.model.ResponseMessage;
import com.careercenter.services.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@RestController
@Tag(name = "Company Controller", description = "Company API")
@ApiResponse(responseCode = "200", description = "Success")
@RequestMapping(value = "/company")
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieves company by volunteer id", description = "Need to pass volunteer id")
    public ResponseEntity<List<Company>> retrieveCompanyByVolunteerId(@PathVariable Long volunteerId) {
        return ResponseEntity.ok().body(companyService.findCompanyByVolunteerId(volunteerId));
    }

    @PostMapping(value = "/save/{volunteerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a New Volunteer", description = "Create a new Volunteer by passing Volunteer request")
    public ResponseEntity<Company> createCompany(@Valid @NotNull @PathVariable Long volunteerId, @Valid @NotNull @RequestBody Company company) {
        return ResponseEntity.ok().body(companyService.saveCompany(volunteerId, company));
    }

    @PutMapping(value = "/update/{volunteerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Company Data", description = "Company Id must be in request.")
    public ResponseEntity<Company> updateCompany(@Valid @NotNull @PathVariable Long volunteerId, @Valid @NotNull @RequestBody Company company) {
        return ResponseEntity.ok().body(companyService.updateCompany(volunteerId, company));
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete an Company By Id", description = "Need to pass Company id")
    public ResponseEntity<ResponseMessage> deleteCompany(@Valid @NotNull @PathVariable long id) {
        return ResponseEntity.ok().body(companyService.deleteCompanyById(id));
    }
}
