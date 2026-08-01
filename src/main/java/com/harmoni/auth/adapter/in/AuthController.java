package com.harmoni.auth.adapter.in;

import com.harmoni.auth.web.dto.JwtDto;
import com.harmoni.auth.application.port.in.AuthenticateUserUseCase;
import com.harmoni.auth.application.port.in.RefreshTokenUseCase;
import com.harmoni.auth.web.dto.LoginDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Primary adapter (controller) for handling HTTP requests related to authentication.
 * This is a driving adapter that receives requests from the outside world (HTTP clients)
 * and directs them to the appropriate use cases in the application layer.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    /**
     * Authenticates a user and returns an access token and refresh token.
     *
     * @param loginDto the login request containing username and password
     * @return ResponseEntity containing the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginDto loginDto) {
        JwtDto response = authenticateUserUseCase.authenticate(
                loginDto.getUsername(),
                loginDto.getPassword()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Refreshes an access token using a valid refresh token.
     *
     * @param refreshToken the refresh token to use for generating a new access token
     * @return ResponseEntity containing the new access and refresh tokens
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtDto> refreshAccessToken(@RequestBody String refreshToken) {
        JwtDto response = refreshTokenUseCase.refresh(refreshToken);
        return ResponseEntity.ok(response);
    }
}