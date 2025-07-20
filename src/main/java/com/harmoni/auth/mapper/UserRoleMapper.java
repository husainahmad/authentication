package com.harmoni.auth.mapper;

import com.harmoni.auth.model.UserRoleKey;
import org.apache.ibatis.annotations.Mapper;

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
     * Retrieves all roles assigned to a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link UserRoleKey} representing the user's roles
     */
    List<UserRoleKey> selectRolesByUserId(Integer userId);

    /**
     * Inserts a new user-role mapping into the database.
     *
     * @param row the user-role key to insert
     * @return number of rows affected (should be 1 if successful)
     */
    int insert(UserRoleKey row);
}
