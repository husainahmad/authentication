package com.harmoni.auth.http.controller;

import com.harmoni.auth.bussines.service.RegistrationService;
import com.harmoni.auth.http.response.RestAPIResponse;
import com.harmoni.auth.model.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling user management operations such as registration,
 * update, and deactivation.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final RegistrationService registrationService;

    /**
     * Registers a new user.
     *
     * @param userDto the user details to be registered
     * @return {@link ResponseEntity} with HTTP 201 status on success
     */
    @PostMapping("")
    public ResponseEntity<RestAPIResponse> register(@Valid @RequestBody UserDto userDto) {
        int row = registrationService.register(userDto);
        log.debug("User Created : {}", row);

        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.CREATED.value())
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.CREATED);
    }

    /**
     * Deactivates a user by username.
     *
     * @param username the username of the user to deactivate
     * @return {@link ResponseEntity} with HTTP 200 status on success
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<RestAPIResponse> deactivate(@PathVariable String username) {
        int row = registrationService.deactivate(username);
        log.debug("User Deactivated : {}", row);

        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.OK);
    }

    /**
     * Updates the user details for the given username.
     *
     * @param username the username to update
     * @param userDto  the updated user information
     * @return {@link ResponseEntity} with HTTP 200 status on success
     */
    @PutMapping("/{username}")
    public ResponseEntity<RestAPIResponse> update(@PathVariable String username, @Valid @RequestBody UserDto userDto) {
        userDto.setUsername(username);
        int row = registrationService.update(userDto);
        log.debug("User Updated : {}", row);

        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.OK);
    }
}
