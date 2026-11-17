package com.harmoni.auth.application.usecase;

import com.harmoni.auth.application.port.in.UserRoleManagementUseCase;
import com.harmoni.auth.domain.model.Role;
import com.harmoni.auth.domain.model.UserRoleKey;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RolePermissionRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RoleRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRoleRepository;
import com.harmoni.auth.web.dto.AssignmentDto;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Application service for assigning scoped roles to users.
 */
@RequiredArgsConstructor
public class UserRoleManagementUseCaseImpl implements UserRoleManagementUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RbacAuditRecorder auditRecorder;

    @Override
    public List<AssignmentDto> findAssignments(Long userId) {
        requireUser(userId);
        return userRoleRepository.findAssignmentsByUserId(userId).stream()
                .map(AssignmentDto::from)
                .toList();
    }

    @Override
    public Map<Long, List<AssignmentDto>> findAssignments(Collection<Long> userIds) {
        Map<Long, List<AssignmentDto>> result = new LinkedHashMap<>();
        userRoleRepository.findAssignmentsByUserIds(userIds).forEach((userId, assignments) ->
                result.put(userId, assignments.stream().map(AssignmentDto::from).toList()));
        return result;
    }

    @Override
    public void replaceAssignments(Long userId, List<AssignmentDto> assignments) {
        requireUser(userId);
        if (assignments != null) {
            for (AssignmentDto assignment : assignments) {
                Role role = assignment.getRoleId() == null ? null : roleRepository.findById(assignment.getRoleId());
                if (role == null) {
                    throw new IllegalArgumentException("Role not found: " + assignment.getRoleId());
                }
                RbacGuard.requireCanAssign(rolePermissionRepository.findPermissionsByRoleId(role.getId()));
            }
        }
        List<UserRoleKey> keys = assignments == null ? List.of()
                : assignments.stream().map(assignment -> assignment.toKey(userId)).toList();
        userRoleRepository.replaceAssignments(userId, keys);
        auditRecorder.record("ASSIGN_ROLES", "USER", userId, String.valueOf(assignments));
    }

    @Override
    public List<Long> findUserIds(Integer roleId, String scopeType) {
        return userRoleRepository.findUserIdsByAssignment(roleId, scopeType);
    }

    private void requireUser(Long userId) {
        if (userId == null || userRepository.findById(userId) == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
    }
}
