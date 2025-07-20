package com.harmoni.auth.bussines.service;

import com.harmoni.auth.component.JwtUtil;
import com.harmoni.auth.exception.BusinessUnAuthorizedRequestException;
import com.harmoni.auth.model.User;
import com.harmoni.auth.model.UserRoleKey;
import com.harmoni.auth.service.UserRoleService;
import com.harmoni.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business service responsible for authenticating users and issuing JWT tokens.
 * <p>
 * This service validates user credentials, retrieves user roles, and generates
 * a signed JWT for successful login attempts.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user by verifying username and password, and returns a JWT token if valid.
     *
     * @param username the user's username
     * @param password the raw password to validate
     * @return a signed JWT token containing the username and user roles
     * @throws BusinessUnAuthorizedRequestException if the username is not found or password is incorrect
     */
    public String authenticate(String username, String password) {
        User user = userService.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessUnAuthorizedRequestException("exception.auth.username.password.notFound", null);
        }

        List<UserRoleKey> userRoles = userRoleService.selectRolesByUserId(user.getId());

        return jwtUtil.generateToken(username, userRoles);
    }
}
