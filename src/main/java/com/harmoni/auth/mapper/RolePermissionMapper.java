package com.harmoni.auth.mapper;

import com.harmoni.auth.model.RolePermissionKey;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis mapper interface for managing the mapping between roles and permissions.
 * <p>
 * This interface handles the many-to-many relationship between {@code Role} and {@code Permission}
 * via the {@link RolePermissionKey} association entity.
 * </p>
 *
 * <p>Typically used in systems implementing Role-Based Access Control (RBAC).</p>
 *
 * @author
 */
@Mapper
public interface RolePermissionMapper {

    /**
     * Deletes a role-permission mapping by its composite key.
     *
     * @param key the composite key identifying the role-permission relationship
     * @return number of rows affected (should be 1 if successful)
     */
    int deleteByPrimaryKey(RolePermissionKey key);

    /**
     * Inserts a new role-permission mapping.
     *
     * @param row the role-permission key representing the relationship to insert
     * @return number of rows affected (should be 1 if successful)
     */
    int insert(RolePermissionKey row);
}
