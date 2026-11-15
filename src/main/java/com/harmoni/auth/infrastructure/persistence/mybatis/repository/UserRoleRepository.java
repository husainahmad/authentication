package com.harmoni.auth.infrastructure.persistence.mybatis.repository;

import com.harmoni.auth.domain.model.Role;
import com.harmoni.auth.domain.model.UserRoleKey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository port for scoped user-role assignments.
 */
public interface UserRoleRepository {

    /**
     * Assigns a role to a user within a scope, ignoring duplicates.
     *
     * @param userId    the user id
     * @param roleId    the role id
     * @param scopeType the scope type (BRAND, CHAIN, STORE)
     * @param scopeId   the brand, chain or store id
     */
    void assign(Long userId, Long roleId, String scopeType, Long scopeId);

    /**
     * Removes every assignment of a role from a user.
     *
     * @param userId the user id
     * @param roleId the role id
     */
    void remove(Long userId, Long roleId);

    /**
     * Replaces all assignments of a user.
     *
     * @param userId      the user id
     * @param assignments the assignments to keep
     */
    void replaceAssignments(Long userId, List<UserRoleKey> assignments);

    /**
     * Finds the assignments of a user.
     *
     * @param userId the user id
     * @return the assignments, including role and scope
     */
    List<UserRoleKey> findAssignmentsByUserId(Long userId);

    /**
     * Finds the assignments of several users in one query.
     *
     * @param userIds the user ids
     * @return assignments keyed by user id
     */
    Map<Long, List<UserRoleKey>> findAssignmentsByUserIds(Collection<Long> userIds);

    /**
     * Finds the distinct roles assigned to a user.
     *
     * @param userId the user id
     * @return the assigned roles
     */
    Set<Role> findRolesByUserId(Long userId);
}
