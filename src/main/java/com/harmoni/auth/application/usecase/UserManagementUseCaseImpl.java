package com.harmoni.auth.application.usecase;

import com.harmoni.auth.domain.model.User;
import com.harmoni.auth.web.dto.UserDto;
import com.harmoni.auth.application.port.in.UserManagementUseCase;
import com.harmoni.auth.application.port.out.PasswordEncoder;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;

/**
 * Use case for managing user operations (registration, update, deactivation).
 * This application service orchestrates the user management process by coordinating
 * between the domain repositories and infrastructure ports.
 * Implements the UserManagementUseCase input port.
 */
public class UserManagementUseCaseImpl implements UserManagementUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementUseCaseImpl(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int register(UserDto userDto) {
        // Check if username already exists
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }

        // Encode password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Create and save user
        User user = userDto.toUser();
        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    @Override
    public int deactivate(String username) {
        // Find user by username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        // Delete user by ID
        boolean deleted = userRepository.deleteById(Long.valueOf(user.getId()));
        return deleted ? 1 : 0;
    }

    @Override
    public int update(UserDto userDto) {
        // Find existing user by username
        User existingUser = userRepository.findByUsername(userDto.getUsername());
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found: " + userDto.getUsername());
        }

        // Encode password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Update user
        User user = userDto.toUser();
        user.setId(existingUser.getId()); // Ensure the ID is set for update

        User updatedUser = userRepository.save(user);

        return updatedUser.getId();
    }
}
