package com.harmoni.auth.bussines.service;

import com.harmoni.auth.exception.BusinessNotFoundRequestException;
import com.harmoni.auth.model.User;
import com.harmoni.auth.model.dto.UserDto;
import com.harmoni.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Business service responsible for user registration, update, and deactivation.
 * <p>
 * This service handles password encoding and basic business validation
 * before delegating persistence operations to {@link UserService}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user after checking for username uniqueness and encoding the password.
     *
     * @param userDto the user data to register
     * @return the ID of the newly created user
     * @throws BusinessNotFoundRequestException if the username already exists
     */
    public int register(UserDto userDto) {
        if (userService.selectByUsername(userDto.getUsername()) != null) {
            throw new BusinessNotFoundRequestException("exception.user.username.exist", null);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.create(userDto.toUser());
    }

    /**
     * Deactivates (soft-deletes or removes) a user by username.
     *
     * @param username the username of the user to deactivate
     * @return result of the deletion operation (typically number of rows affected)
     * @throws BusinessNotFoundRequestException if the user is not found
     */
    public int deactivate(String username) {
        User user = userService.selectByUsername(username);
        if (user == null) {
            throw new BusinessNotFoundRequestException("exception.user.username.notFound", null);
        }
        return userService.deleteByUsername(user);
    }

    /**
     * Updates user information (including password) by username.
     *
     * @param userDto the new user data
     * @return result of the update operation (typically number of rows affected)
     * @throws BusinessNotFoundRequestException if the user does not exist
     */
    public int update(UserDto userDto) {
        User user = userService.selectByUsername(userDto.getUsername());
        if (user == null) {
            throw new BusinessNotFoundRequestException("exception.user.username.notFound", null);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.updateByUsername(userDto.toUser());
    }
}
