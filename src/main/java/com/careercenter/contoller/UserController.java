package com.careercenter.contoller;

import com.careercenter.model.ResponseMessage;
import com.careercenter.entities.User;
import com.careercenter.model.UserResponse;
import com.careercenter.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@RestController
@Tag(name = "User Controller", description = "User API")
@ApiResponse(responseCode = "200", description = "Success")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieves All Users", description = "No need to pass parameters")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponse>> retrieveAllUsers() {
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieves an User By Id", description = "Need to pass user id")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> retrieveUserById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieves an User By Email", description = "Need to pass user email")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> retrieveUserByEmail(@Email(message = "Invalid email.") @PathVariable String email) {
        return ResponseEntity.ok().body(userService.findUserByEmail(email));
    }

    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieves an User By Username", description = "Need to pass user username")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> retrieveUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.findUserByUsername(username));
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update User Data", description = "User Id must be in request.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@Valid @NotNull @RequestBody @Parameter(description = "Pass an user as request") User user) {
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete an User By Id", description = "Need to pass user id")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteUser(@Valid @NotNull @PathVariable
                                                      @Parameter(description = "Pass an user id in request") Long id) {
        return ResponseEntity.ok().body(userService.deleteUser(id));
    }

}
