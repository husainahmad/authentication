package com.harmoni.auth.mapper;

import com.harmoni.auth.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis mapper interface for performing CRUD operations on the {@link User} entity.
 * <p>
 * Provides methods to interact with the underlying user table in the database.
 * This includes inserting, updating, selecting, and deleting users by their primary key or username.
 * </p>
 *
 * Typically used in user authentication and management services.
 *
 * @author
 */
@Mapper
public interface UserMapper {

    /**
     * Inserts a new user record into the database.
     *
     * @param row the user object to insert
     * @return number of rows affected (should be 1 if successful)
     */
    int insert(User row);

    /**
     * Updates an existing user identified by the primary key.
     *
     * @param row the user object with updated fields
     * @return number of rows affected (should be 1 if successful)
     */
    int updateByPrimaryKey(User row);

    /**
     * Selects a user by its primary key (user ID).
     *
     * @param id the ID of the user
     * @return the corresponding {@link User} object, or null if not found
     */
    User selectByPrimaryKey(Integer id);

    /**
     * Selects a user by their unique username.
     *
     * @param username the username of the user
     * @return the corresponding {@link User} object, or null if not found
     */
    User selectByUsername(String username);

    /**
     * Deletes a user record by username.
     *
     * @param username the username of the user to delete
     * @return number of rows affected (should be 1 if successful)
     */
    int deleteByUsername(String username);
}
