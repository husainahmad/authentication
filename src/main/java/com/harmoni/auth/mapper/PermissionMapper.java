package com.harmoni.auth.mapper;

import com.harmoni.auth.model.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis mapper interface for performing CRUD operations on {@link Permission} entities.
 * <p>
 * This interface defines the basic database operations for the {@code permission} table.
 * MyBatis will generate the implementation at runtime.
 * </p>
 *
 * <p>Expected to be used in service layers to abstract direct database interactions.</p>
 *
 * @author husainahmad
 */
@Mapper
public interface PermissionMapper {

    /**
     * Inserts a new {@link Permission} record into the database.
     *
     * @param row the {@link Permission} entity to insert
     * @return number of rows affected (should be 1 if successful)
     */
    int insert(Permission row);

    /**
     * Selects a {@link Permission} by its primary key (ID).
     *
     * @param id the ID of the permission
     * @return the {@link Permission} entity, or {@code null} if not found
     */
    Permission selectByPrimaryKey(Long id);

    /**
     * Updates a {@link Permission} record based on its primary key.
     *
     * @param row the {@link Permission} entity with updated fields
     * @return number of rows affected (should be 1 if successful)
     */
    int updateByPrimaryKey(Permission row);

    /**
     * Deletes a {@link Permission} record by its primary key (ID).
     *
     * @param id the ID of the permission to delete
     * @return number of rows affected (should be 1 if successful)
     */
    int deleteByPrimaryKey(Long id);
}
