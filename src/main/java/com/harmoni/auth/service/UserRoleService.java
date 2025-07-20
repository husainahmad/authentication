package com.harmoni.auth.service;

import com.harmoni.auth.mapper.UserRoleMapper;
import com.harmoni.auth.model.UserRoleKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling user-role related operations.
 * <p>
 * This service provides methods to retrieve roles assigned to a specific user.
 * It acts as a business-layer abstraction on top of {@link UserRoleMapper}.
 * </p>
 *
 * @author @husainahmad
 */
@Service("userRoleService")
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleMapper userRoleMapper;

    /**
     * Retrieves the list of roles associated with a user by their user ID.
     *
     * @param userId the ID of the user whose roles are to be fetched
     * @return a list of {@link UserRoleKey} representing the user's roles
     */
    public List<UserRoleKey> selectRolesByUserId(Integer userId) {
        return userRoleMapper.selectRolesByUserId(userId);
    }
}
