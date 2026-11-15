package com.harmoni.auth.infrastructure.persistence.mybatis.repository;

import com.harmoni.auth.domain.model.Permission;

import java.util.Set;

/**
 * Repository port for the role-permission relationship.
 */
public interface RolePermissionRepository {

    /**
     * Grants a permission to a role, ignoring duplicates.
     *
     * @param roleId       the role id
     * @param permissionId the permission id
     */
    void assign(Long roleId, Long permissionId);

    /**
     * Revokes a permission from a role.
     *
     * @param roleId       the role id
     * @param permissionId the permission id
     */
    void remove(Long roleId, Long permissionId);

    /**
     * Replaces all permissions granted to a role.
     *
     * @param roleId        the role id
     * @param permissionIds the permission ids to keep
     */
    void replacePermissions(Long roleId, Set<Long> permissionIds);

    /**
     * Finds the permissions granted to a role.
     *
     * @param roleId the role id
     * @return the granted permissions
     */
    Set<Permission> findPermissionsByRoleId(Long roleId);
}
