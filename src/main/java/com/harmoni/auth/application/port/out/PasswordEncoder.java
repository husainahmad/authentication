package com.harmoni.auth.application.port.out;

/**
 * Port (interface) for password encoding operations.
 * Defines the contract for password encoding and verification that adapters must implement.
 */
public interface PasswordEncoder {

    /**
     * Encodes a raw password.
     *
     * @param rawPassword the plain text password to encode
     * @return the encoded password
     */
    String encode(String rawPassword);

    /**
     * Checks if a raw password matches an encoded password.
     *
     * @param rawPassword     the plain text password to check
     * @param encodedPassword the encoded password to check against
     * @return true if the raw password matches the encoded password, false otherwise
     */
    boolean matches(String rawPassword, String encodedPassword);
}