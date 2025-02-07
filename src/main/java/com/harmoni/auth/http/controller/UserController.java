package com.harmoni.auth.http.controller;

import com.harmoni.auth.http.response.RestAPIResponse;
import com.harmoni.auth.model.dto.UserDto;
import com.harmoni.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RestAPIResponse> register(@Valid @RequestBody UserDto userDto) {

        int row = userService.create(userDto);
        log.debug("User Created : {}", row);
        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.CREATED.value())
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.CREATED);

    }

}
