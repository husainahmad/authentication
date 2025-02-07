package com.harmoni.auth.http.controller;

import com.harmoni.auth.http.response.RestAPIResponse;
import com.harmoni.auth.model.dto.LoginDto;
import com.harmoni.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<RestAPIResponse> login(@Valid @RequestBody LoginDto loginDto) {
        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.OK.value())
                .data(authService.authenticate(loginDto.getUsername(), loginDto.getPassword()))
                .error(null)
                .build();
        return new ResponseEntity<>(restAPIResponse, HttpStatus.OK);
    }
}
