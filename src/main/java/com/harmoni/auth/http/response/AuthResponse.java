package com.harmoni.auth.http.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A response object containing access and refresh tokens.
 */
@Getter
@AllArgsConstructor
public class AuthResponse {
    private final String accessToken;
    private final String refreshToken;
}
