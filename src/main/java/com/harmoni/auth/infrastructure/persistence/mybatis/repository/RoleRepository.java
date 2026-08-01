package com.harmoni.auth.infrastructure.persistence.mybatis.repository;

import com.harmoni.auth.domain.model.Role;

import java.util.List;
import java.util.Set;

/**
 * Repository port for Role entity.
 * Defines the contract for role data access operations.
 */
public interface RoleRepository {

    /**
     * Saves a role.
     *
     * @param role the role to save
     * @return the saved role
     */
    Role save(Role role);

    /**
     * Finds a role by their ID.
     *
     * @param id the ID of the role to find
     * @return the role if found, null otherwise
     */
    Role findById(Long id);

    /**
     * Finds a role by their name.
     *
     * @param name the name of the role to find
     * @return the role if found, null otherwise
     */
    Role findByName(String name);

    /**
     * Finds all roles.
     *
     * @return a list of all roles
     */
    java.util.List<Role> findAll();

    /**
     * Deletes a role by their ID.
     *
     * @param id the ID of the role to delete
     * @return true if the role was deleted, false otherwise
     */
    boolean deleteById(Long id);

    /**
     * Finds roles by user ID.
     *
     * @param userId the ID of the user
     * @return a set of roles associated with the user
     */
    Set<Role> findRolesByUserId(Long userId);
}