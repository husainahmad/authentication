package com.harmoni.auth.infrastructure.persistence.mybatis.repository;

import com.harmoni.auth.domain.model.User;

/**
 * Repository port for User entity.
 * Defines the contract for user data access operations.
 * Implementations of this interface are adapters that connect to specific data sources.
 */
public interface UserRepository {

    /**
     * Saves a user.
     *
     * @param user the user to save
     * @return the saved user
     */
    User save(User user);

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user if found, null otherwise
     */
    User findById(Long id);

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return the user if found, null otherwise
     */
    User findByUsername(String username);

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return true if the user was deleted, false otherwise
     */
    boolean deleteById(Long id);
}