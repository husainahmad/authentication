package com.harmoni.auth.web.dto;

import com.harmoni.auth.domain.model.UserRoleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A role assignment with its scope.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDto {

    private Long id;
    private Long roleId;
    private String roleName;
    private String scopeType;
    private Long scopeId;

    public static AssignmentDto from(UserRoleKey assignment) {
        if (assignment == null) {
            return null;
        }
        return AssignmentDto.builder()
                .id(assignment.getId())
                .roleId(assignment.getRoleId())
                .roleName(assignment.getRole() == null ? null : assignment.getRole().getName())
                .scopeType(assignment.getScopeType())
                .scopeId(assignment.getScopeId())
                .build();
    }

    public UserRoleKey toKey(Long userId) {
        return new UserRoleKey()
                .setUserId(userId)
                .setRoleId(roleId)
                .setScopeType(scopeType)
                .setScopeId(scopeId);
    }
}
