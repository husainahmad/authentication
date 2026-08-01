package com.harmoni.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Composite key for role-permission relationship.
 * Used in MyBatis mapping for role-permission associations.
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolePermissionKey {
    private Long roleId;
    private Long permissionId;
}