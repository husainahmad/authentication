package com.harmoni.auth.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object for authentication responses.
 * Contains the access token and refresh token returned after successful authentication or token refresh.
 */
@Data
@Builder
@AllArgsConstructor
public class JwtDto {
    private String accessToken;
    private String refreshToken;
}
