package com.harmoni.auth.mapper;

import com.harmoni.auth.model.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis mapper interface for performing CRUD operations on {@link RefreshToken} entities.
 * <p>
 * This interface handles database interactions related to refresh tokens, which are used
 * for session continuation without requiring re-authentication.
 * </p>
 *
 * <p>Typically used in authentication and token management services.</p>
 *
 * @author
 */
@Mapper
public interface RefreshTokenMapper {

    /**
     * Deletes a {@link RefreshToken} record by its primary key (ID).
     *
     * @param id the ID of the refresh token to delete
     * @return number of rows affected (should be 1 if successful)
     */
    int deleteByPrimaryKey(Long id);

    /**
     * Inserts a new {@link RefreshToken} record into the database.
     *
     * @param row the {@link RefreshToken} entity to insert
     * @return number of rows affected (should be 1 if successful)
     */
    int insert(RefreshToken row);

    /**
     * Retrieves a {@link RefreshToken} record by its primary key (ID).
     *
     * @param id the ID of the refresh token
     * @return the {@link RefreshToken} entity, or {@code null} if not found
     */
    RefreshToken selectByPrimaryKey(Long id);

    /**
     * Updates an existing {@link RefreshToken} record identified by its primary key.
     *
     * @param row the {@link RefreshToken} entity with updated data
     * @return number of rows affected (should be 1 if successful)
     */
    int updateByPrimaryKey(RefreshToken row);
}
