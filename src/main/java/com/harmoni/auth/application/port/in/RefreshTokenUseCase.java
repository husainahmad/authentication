package com.harmoni.auth.application.port.in;

import com.harmoni.auth.web.dto.JwtDto;

/**
 * Input port for the refresh token use case.
 * Defines the contract for refreshing an access token using a refresh token.
 */
public interface RefreshTokenUseCase {

    /**
     * Refreshes an access token using the provided refresh token.
     *
     * @param refreshToken the refresh token to use for generating new tokens
     * @return an AuthResponseDto containing the new access and refresh tokens
     */
    JwtDto refresh(String refreshToken);
}