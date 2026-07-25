package com.harmoni.auth.http.controller;

import com.harmoni.auth.http.response.AuthResponse;
import com.harmoni.auth.http.response.RestAPIResponse;
import com.harmoni.auth.model.dto.LoginDto;
import com.harmoni.auth.bussines.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for handling authentication-related endpoints.
 * <p>
 * Provides an endpoint for user login and returns a JWT token upon successful authentication.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user using the provided login credentials.
     *
     * @param loginDto the login request payload containing username and password
     * @return a {@link ResponseEntity} containing the {@link RestAPIResponse} with a JWT token on success
     */
    @PostMapping("/login")
    public ResponseEntity<RestAPIResponse> login(@Valid @RequestBody LoginDto loginDto) {
        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.OK.value())
                .data(authService.authenticate(loginDto.getUsername(), loginDto.getPassword()))
                .error(null)
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.OK);
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param refreshToken the refresh token
     * @return a {@link ResponseEntity} containing the new access and refresh tokens
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<RestAPIResponse> refreshAccessToken(@RequestBody String refreshToken) {
        AuthResponse authResponse = authService.refreshAccessToken(refreshToken);
        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.OK.value())
                .data(authResponse)
                .error(null)
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.OK);
    }
}
