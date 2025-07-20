package com.harmoni.auth.service;

import com.harmoni.auth.mapper.UserMapper;
import com.harmoni.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user-related operations.
 * <p>
 * Acts as an abstraction layer between the controller/business logic and the {@link UserMapper},
 * which handles direct database interactions.
 * </p>
 *
 * <p>Supported operations:</p>
 * <ul>
 *     <li>Create a new user</li>
 *     <li>Fetch a user by username</li>
 *     <li>Delete a user by username</li>
 *     <li>Update a user by username</li>
 * </ul>
 *
 * @author husainahmad
 */
@Service("userService")
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    /**
     * Creates a new user in the database.
     *
     * @param user the {@link User} entity to be inserted
     * @return number of rows affected
     */
    public int create(User user) {
        return userMapper.insert(user);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @return the matching {@link User}, or {@code null} if not found
     */
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * Deletes a user based on their username.
     *
     * @param user the {@link User} object (username will be used for deletion)
     * @return number of rows affected
     */
    public int deleteByUsername(User user) {
        return userMapper.deleteByUsername(user.getUsername());
    }

    /**
     * Updates an existing user using the primary key.
     *
     * @param user the updated {@link User} object
     * @return number of rows affected
     */
    public int updateByUsername(User user) {
        return userMapper.updateByPrimaryKey(user);
    }
}
