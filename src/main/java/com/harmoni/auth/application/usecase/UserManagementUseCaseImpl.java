package com.harmoni.auth.application.usecase;

import com.harmoni.auth.domain.model.User;
import com.harmoni.auth.domain.model.UserRoleKey;
import com.harmoni.auth.web.dto.AssignmentDto;
import com.harmoni.auth.web.dto.UserDto;
import com.harmoni.auth.application.port.in.UserManagementUseCase;
import com.harmoni.auth.application.port.out.PasswordEncoder;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RolePermissionRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRoleRepository;

import java.util.List;

/**
 * Use case for managing user operations (registration, update, deactivation).
 * This application service orchestrates the user management process by coordinating
 * between the domain repositories and infrastructure ports.
 * Implements the UserManagementUseCase input port.
 */
public class UserManagementUseCaseImpl implements UserManagementUseCase {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementUseCaseImpl(UserRepository userRepository,
                                     UserRoleRepository userRoleRepository,
                                     RolePermissionRepository rolePermissionRepository,
                                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int register(UserDto userDto) {
        // Check if username already exists
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }

        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Encode password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        requireCanAssign(userDto);

        // Create and save user
        User user = userDto.toUser();
        user.setEnabled(userDto.getEnabled() == null || userDto.getEnabled());
        User savedUser = userRepository.save(user);
        userRoleRepository.replaceAssignments(Long.valueOf(savedUser.getId()),
                toAssignments(savedUser.getId(), userDto));

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

        // Encode a new password, or keep the existing one when the edit payload
        // leaves it blank.
        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            userDto.setPassword(existingUser.getPassword());
        } else {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        requireCanAssign(userDto);

        // Update user, preserving the fields the edit payload does not carry
        User user = userDto.toUser();
        user.setId(existingUser.getId()); // Ensure the ID is set for update
        user.setEnabled(userDto.getEnabled() == null ? existingUser.isEnabled() : userDto.getEnabled());
        user.setUpdatedAt(new java.util.Date());

        User updatedUser = userRepository.save(user);
        userRoleRepository.replaceAssignments(Long.valueOf(updatedUser.getId()),
                toAssignments(updatedUser.getId(), userDto));

        return updatedUser.getId();
    }

    private void requireCanAssign(UserDto userDto) {
        if (userDto.getAssignments() != null) {
            for (AssignmentDto assignment : userDto.getAssignments()) {
                if (assignment.getRoleId() != null) {
                    RbacGuard.requireCanAssign(
                            rolePermissionRepository.findPermissionsByRoleId(assignment.getRoleId()));
                }
            }
            return;
        }
        if (userDto.getRoleIds() != null) {
            for (Long roleId : userDto.getRoleIds()) {
                if (roleId != null) {
                    RbacGuard.requireCanAssign(rolePermissionRepository.findPermissionsByRoleId(roleId));
                }
            }
        }
    }

    private List<UserRoleKey> toAssignments(Integer userId, UserDto userDto) {
        if (userDto.getAssignments() != null) {
            return userDto.getAssignments().stream()
                    .map(assignment -> assignment.toKey(Long.valueOf(userId)))
                    .toList();
        }
        if (userDto.getRoleIds() != null) {
            return userDto.getRoleIds().stream()
                    .map(roleId -> new UserRoleKey()
                            .setUserId(Long.valueOf(userId))
                            .setRoleId(roleId)
                            .setScopeType("STORE")
                            .setScopeId(0L))
                    .toList();
        }
        return List.of();
    }
}
