package com.harmoni.auth.infrastructure.persistence.mybatis.repository;

import com.harmoni.auth.domain.model.RefreshToken;

import java.util.Date;
import java.util.Optional;

/**
 * Repository port for RefreshToken entity.
 * Defines the contract for refresh token data access operations.
 */
public interface RefreshTokenRepository {

    /**
     * Saves a refresh token.
     *
     * @param refreshToken the refresh token to save
     * @return the saved refresh token
     */
    RefreshToken save(RefreshToken refreshToken);

    /**
     * Finds a refresh token by their ID.
     *
     * @param id the ID of the refresh token to find
     * @return the refresh token if found, null otherwise
     */
    RefreshToken findById(Long id);

    /**
     * Finds a refresh token by their token value.
     *
     * @param token the token value to search for
     * @return the refresh token if found, null otherwise
     */
    RefreshToken findByToken(String token);

    /**
     * Finds a refresh token by username.
     *
     * @param username the username associated with the refresh token
     * @return the refresh token if found, null otherwise
     */
    RefreshToken findByUsername(String username);

    /**
     * Deletes a refresh token by their ID.
     *
     * @param id the ID of the refresh token to delete
     * @return true if the refresh token was deleted, false otherwise
     */
    boolean deleteById(Long id);

    /**
     * Deletes a refresh token by their token value.
     *
     * @param token the token value to delete
     * @return true if the refresh token was deleted, false otherwise
     */
    boolean deleteByToken(String token);

    /**
     * Deletes expired refresh tokens.
     * @return the number of deleted tokens
     */
    int deleteExpired(Date date);
}