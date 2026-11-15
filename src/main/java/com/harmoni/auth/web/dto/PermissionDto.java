package com.harmoni.auth.web.dto;

import com.harmoni.auth.domain.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transport object for a permission.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {

    private Long id;
    private String name;
    private String description;
    private String module;
    private String action;
    private String category;
    private String label;

    public Permission toPermission() {
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        permission.setDescription(description);
        permission.setModule(module);
        permission.setAction(action);
        permission.setCategory(category);
        permission.setLabel(label);
        return permission;
    }

    public static PermissionDto from(Permission permission) {
        if (permission == null) {
            return null;
        }
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .module(permission.getModule())
                .action(permission.getAction())
                .category(permission.getCategory())
                .label(permission.getLabel())
                .build();
    }
}
