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

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final RegistrationService registrationService;

    @PostMapping("")
    public ResponseEntity<RestAPIResponse> register(@Valid @RequestBody UserDto userDto) {
        int row = registrationService.register(userDto);
        log.debug("User Created : {}", row);
        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.CREATED.value())
                .build();
        return new ResponseEntity<>(restAPIResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<RestAPIResponse> deactivate(@PathVariable String username) {
        int row = registrationService.deactivate(username);
        log.debug("User Deactivated : {}", row);
        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(restAPIResponse, HttpStatus.OK);
    }

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
