package com.harmoni.auth.infrastructure.persistence.mybatis.mapper;

import com.harmoni.auth.domain.model.UserRoleKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * MyBatis mapper interface for managing relationships between users and roles.
 * <p>
 * This interface handles database operations for the user-role mapping table,
 * which typically represents a many-to-many relationship between users and roles.
 * </p>
 *
 * Useful for role-based access control (RBAC) systems.
 */
@Mapper
public interface UserRoleMapper {

    /**
     * Deletes a specific user-role mapping from the database.
     *
     * @param key the composite key containing user ID and role ID
     * @return number of rows affected (should be 1 if successful)
     */
    int deleteByPrimaryKey(UserRoleKey key);

    /**
     * Deletes every role assignment for a user.
     *
     * @param userId the user id
     * @return number of rows affected
     */
    int deleteByUserId(Long userId);

    /**
     * Retrieves all roles assigned to a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link UserRoleKey} representing the user's roles
     */
    List<UserRoleKey> selectRolesByUserId(Integer userId);

    /**
     * Retrieves the assignments for several users in one query.
     *
     * @param userIds the user ids
     * @return the assignments, including role and scope
     */
    List<UserRoleKey> selectAssignmentsByUserIds(@Param("userIds") Collection<Long> userIds);

    /**
     * Inserts a new user-role mapping into the database.
     *
     * @param row the user-role key to insert
     * @return number of rows affected (should be 1 if successful)
     */
    int insert(UserRoleKey row);
}
