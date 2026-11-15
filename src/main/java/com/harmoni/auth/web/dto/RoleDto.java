package com.harmoni.auth.web.dto;

import com.harmoni.auth.domain.model.Permission;
import com.harmoni.auth.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Transport object for a role and its granted permissions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;
    private String name;
    private String description;
    private String scopeType;
    private String code;
    private boolean system;
    private Integer employeeCount;
    private Set<PermissionDto> permissions;

    public Role toRole() {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        role.setScopeType(scopeType);
        role.setCode(code);
        role.setSystem(system);
        return role;
    }

    public static RoleDto from(Role role) {
        if (role == null) {
            return null;
        }
        Set<PermissionDto> permissions = role.getPermissions().stream()
                .map(PermissionDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .scopeType(role.getScopeType())
                .code(role.getCode())
                .system(role.isSystem())
                .employeeCount(role.getEmployeeCount())
                .permissions(permissions)
                .build();
    }

    public static RoleDto from(Role role, Set<Permission> permissions) {
        RoleDto dto = from(role);
        if (dto != null && permissions != null) {
            dto.setPermissions(permissions.stream()
                    .map(PermissionDto::from)
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return dto;
    }
}
