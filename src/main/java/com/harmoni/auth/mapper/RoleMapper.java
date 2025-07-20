package com.harmoni.auth.mapper;

import com.harmoni.auth.model.Role;
import org.apache.ibatis.annotations.Mapper;

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
}
