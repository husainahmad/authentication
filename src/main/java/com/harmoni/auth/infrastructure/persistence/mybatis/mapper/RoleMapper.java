package com.harmoni.auth.infrastructure.persistence.mybatis.mapper;

import com.harmoni.auth.domain.model.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * MyBatis mapper interface for managing {@link Role} entities.
 * <p>
 * Provides methods to perform CRUD (Create, Read, Update, Delete) operations
 * on roles in the authentication system. These roles represent user access levels or permissions groups.
 * </p>
 *
 * <p>This interface is typically used by services that manage user authorization and access control.</p>
 *
 * @author
 */
@Mapper
public interface RoleMapper {

    /**
     * Deletes a {@link Role} record by its primary key (ID).
     *
     * @param id the ID of the role to delete
     * @return number of rows affected (should be 1 if successful)
     */
    int deleteByPrimaryKey(Long id);

    /**
     * Inserts a new {@link Role} record into the database.
     *
     * @param row the {@link Role} entity to insert
     * @return number of rows affected (should be 1 if successful)
     */
    int insert(Role row);

    /**
     * Retrieves a {@link Role} record by its primary key (ID).
     *
     * @param id the ID of the role to retrieve
     * @return the {@link Role} entity, or {@code null} if not found
     */
    Role selectByPrimaryKey(Long id);

    /**
     * Updates an existing {@link Role} record identified by its primary key.
     *
     * @param row the {@link Role} entity with updated data
     * @return number of rows affected (should be 1 if successful)
     */
    int updateByPrimaryKey(Role row);

    /**
     * Selects a {@link Role} by its name.
     *
     * @param name the name of the role
     * @return the {@link Role} entity, or {@code null} if not found
     */
    Role selectByName(String name);

    /**
     * Selects all {@link Role} records.
     *
     * @return a list of all {@link Role} entities
     */
    List<Role> selectAll();

    /**
     * Selects roles by user ID.
     *
     * @param userId the ID of the user
     * @return a list of roles assigned to the user
     */
    List<Role> selectRolesByUserId(Long userId);
}
