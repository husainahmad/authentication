package com.harmoni.auth.application.usecase;

import com.harmoni.auth.web.dto.AssignmentDto;
import com.harmoni.auth.web.dto.JwtDto;
import com.harmoni.auth.application.exception.AuthenticationException;
import com.harmoni.auth.application.port.in.RefreshTokenUseCase;
import com.harmoni.auth.application.port.out.JwtTokenProvider;
import com.harmoni.auth.domain.model.Permission;
import com.harmoni.auth.domain.model.Role;
import com.harmoni.auth.domain.model.UserRoleKey;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.PermissionRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RefreshTokenRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRoleRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Use case for refreshing an access token using a refresh token.
 * This application service orchestrates the token refresh process.
 * Implements the RefreshTokenUseCase input port.
 */
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenUseCaseImpl(RefreshTokenRepository refreshTokenRepository,
                                   UserRepository userRepository,
                                   UserRoleRepository userRoleRepository,
                                   PermissionRepository permissionRepository,
                                   JwtTokenProvider jwtTokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.permissionRepository = permissionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Refreshes an access token using the provided refresh token.
     *
     * @param refreshToken the refresh token to use for generating new tokens
     * @return an AuthResponseDto containing the new access and refresh tokens
     * @throws AuthenticationException if the refresh token is invalid or expired
     */
    @Override
    public JwtDto refresh(String refreshToken) {
        // Find the refresh token in the repository
        var tokenEntity = refreshTokenRepository.findByToken(refreshToken);
        if (tokenEntity == null) {
            throw new AuthenticationException("Invalid refresh token");
        }

        // Check if the token is expired
        if (tokenEntity.isExpired()) {
            throw new AuthenticationException("Expired refresh token");
        }

        // Find the user associated with the refresh token
        var user = userRepository.findById(tokenEntity.getUserId());
        if (user == null) {
            throw new AuthenticationException("User not found for refresh token");
        }

        // Reload scoped assignments, roles and permissions so a refreshed token
        // reflects current grants
        List<UserRoleKey> assignments = userRoleRepository.findAssignmentsByUserId(Long.valueOf(user.getId()));
        Set<Role> roles = new HashSet<>();
        assignments.stream()
                .map(UserRoleKey::getRole)
                .filter(Objects::nonNull)
                .forEach(roles::add);

        Set<String> permissions = new HashSet<>();
        for (Role role : roles) {
            Set<Permission> rolePermissions = permissionRepository.findPermissionsByRoleId(role.getId());
            rolePermissions.forEach(role::addPermission);
            rolePermissions.forEach(permission -> permissions.add(permission.getName()));
        }
        List<AssignmentDto> scopes = assignments.stream()
                .map(AssignmentDto::from)
                .toList();

        // Generate new access and refresh tokens using the JWT provider port
        List<Role> rolesList = new ArrayList<>(roles);
        String newAccessToken = jwtTokenProvider.generateToken(
                user.getId(),
                user.getUsername(),
                rolesList,
                permissions,
                scopes
        );

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return new JwtDto(newAccessToken, newRefreshToken);
    }
}
