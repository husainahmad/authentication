package com.harmoni.auth.application.port.out;

import java.util.List;
import com.harmoni.auth.domain.model.Role;

/**
 * Port (interface) for JWT token operations.
 * Defines the contract for JWT token generation and validation that adapters must implement.
 */
public interface JwtTokenProvider {

    /**
     * Generates an access token for the given user with their roles.
     *
     * @param userId   the user id to embed as a claim
     * @param username the username to include in the token
     * @param roles    the roles to include in the token
     * @return a signed JWT token
     */
    String generateToken(Integer userId, String username, List<Role> roles);

    /**
     * Generates a refresh token for the given user.
     *
     * @param username the username to include in the token
     * @return a signed refresh token
     */
    String generateRefreshToken(String username);

    /**
     * Extracts the username from a JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) from the token
     */
    String extractUsername(String token);

    /**
     * Validates a JWT token for a given username.
     *
     * @param token    the JWT token to validate
     * @param username the expected username
     * @return true if the token is valid and matches the username, false otherwise
     */
    boolean validateToken(String token, String username);
}