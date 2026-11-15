package com.harmoni.auth.application.usecase;

import com.harmoni.auth.application.port.in.AuthoritiesQueryUseCase;
import com.harmoni.auth.domain.model.Permission;
import com.harmoni.auth.domain.model.Role;
import com.harmoni.auth.domain.model.User;
import com.harmoni.auth.domain.model.UserRoleKey;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.PermissionRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRoleRepository;
import com.harmoni.auth.web.dto.AssignmentDto;
import com.harmoni.auth.web.dto.AuthoritiesDto;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Application service that resolves the effective roles, permissions and scopes
 * of a user.
 */
@RequiredArgsConstructor
public class AuthoritiesQueryUseCaseImpl implements AuthoritiesQueryUseCase {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public AuthoritiesDto findByUserId(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return build(user);
    }

    @Override
    public AuthoritiesDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        return build(user);
    }

    private AuthoritiesDto build(User user) {
        List<UserRoleKey> assignments = userRoleRepository.findAssignmentsByUserId(Long.valueOf(user.getId()));

        Set<String> roleNames = new LinkedHashSet<>();
        assignments.stream()
                .map(UserRoleKey::getRole)
                .filter(Objects::nonNull)
                .map(Role::getName)
                .forEach(roleNames::add);

        Set<Permission> permissions = permissionRepository.findPermissionsByUserId(Long.valueOf(user.getId()));
        Set<String> permissionNames = new LinkedHashSet<>();
        permissions.forEach(permission -> permissionNames.add(permission.getName()));

        List<AssignmentDto> scopes = assignments.stream()
                .map(AssignmentDto::from)
                .toList();

        return AuthoritiesDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .roles(roleNames)
                .permissions(permissionNames)
                .scopes(scopes)
                .build();
    }
}
