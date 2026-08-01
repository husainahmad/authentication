package com.harmoni.auth.application.port.in;

import com.harmoni.auth.web.dto.UserDto;

/**
 * Input port for user management use cases.
 * Defines the contract for user registration, update, and deactivation operations.
 */
public interface UserManagementUseCase {

    /**
     * Registers a new user after checking for username uniqueness and encoding the password.
     *
     * @param userDto the user data to register
     * @return the ID of the newly created user
     * @throws IllegalArgumentException if the username already exists
     */
    int register(UserDto userDto);

    /**
     * Deactivates (soft-deletes or removes) a user by username.
     *
     * @param username the username of the user to deactivate
     * @return result of the deletion operation (typically number of rows affected)
     * @throws IllegalArgumentException if the user is not found
     */
    int deactivate(String username);

    /**
     * Updates user information (including password) by username.
     *
     * @param userDto the new user data
     * @return result of the update operation (typically number of rows affected)
     * @throws IllegalArgumentException if the user does not exist
     */
    int update(UserDto userDto);
}