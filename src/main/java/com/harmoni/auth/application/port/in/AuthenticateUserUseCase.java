package com.harmoni.auth.application.port.in;

import com.harmoni.auth.web.dto.JwtDto;

/**
 * Input port for the authenticate user use case.
 * Defines the contract for authenticating a user with username and password.
 */
public interface AuthenticateUserUseCase {

    /**
     * Authenticates a user with the given username and password.
     *
     * @param username the username to authenticate
     * @param password the plain text password to verify
     * @return an AuthResponseDto containing the access and refresh tokens
     */
    JwtDto authenticate(String username, String password);
}