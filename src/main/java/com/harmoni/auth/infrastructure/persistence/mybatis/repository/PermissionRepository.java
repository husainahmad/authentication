package com.harmoni.auth.infrastructure.persistence.mybatis.repository;

import com.harmoni.auth.domain.model.Permission;

import java.util.List;
import java.util.Set;

/**
 * Repository port for Permission entity.
 * Defines the contract for permission data access operations.
 */
public interface PermissionRepository {

    /**
     * Saves a permission.
     *
     * @param permission the permission to save
     * @return the saved permission
     */
    Permission save(Permission permission);

    /**
     * Finds a permission by their ID.
     *
     * @param id the ID of the permission to find
     * @return the permission if found, null otherwise
     */
    Permission findById(Long id);

    /**
     * Finds a permission by their name.
     *
     * @param name the name of the permission to find
     * @return the permission if found, null otherwise
     */
    Permission findByName(String name);

    /**
     * Finds all permissions.
     *
     * @return a list of all permissions
     */
    java.util.List<Permission> findAll();

    /**
     * Deletes a permission by their ID.
     *
     * @param id the ID of the permission to delete
     * @return true if the permission was deleted, false otherwise
     */
    boolean deleteById(Long id);

    /**
     * Finds permissions by role ID.
     *
     * @param roleId the ID of the role
     * @return a set of permissions associated with the role
     */
    Set<Permission> findPermissionsByRoleId(Long roleId);

    /**
     * Finds permissions by user ID.
     *
     * @param userId the ID of the user
     * @return a set of permissions associated with the user (through their roles)
     */
    Set<Permission> findPermissionsByUserId(Long userId);
}