package com.harmoni.auth.application.usecase;

import com.harmoni.auth.web.dto.JwtDto;
import com.harmoni.auth.application.exception.AuthenticationException;
import com.harmoni.auth.application.port.in.AuthenticateUserUseCase;
import com.harmoni.auth.application.port.out.JwtTokenProvider;
import com.harmoni.auth.application.port.out.PasswordEncoder;
import com.harmoni.auth.domain.model.Role;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RoleRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Use case for authenticating a user.
 * This application service orchestrates the authentication process by coordinating
 * between the domain services, repositories, and infrastructure ports.
 * Implements the AuthenticateUserUseCase input port.
 */
@RequiredArgsConstructor
public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user with the given username and password.
     *
     * @param username the username to authenticate
     * @param password the plain text password to verify
     * @return an AuthResponseDto containing the access and refresh tokens
     * @throws AuthenticationException if authentication fails
     */
    @Override
    public JwtDto authenticate(String username, String password) {
        // Retrieve user from repository
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AuthenticationException("User not found: " + username);
        }

        // Validate password using the password encoder port
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid credentials for user: " + username);
        }

        // Load user's roles
        user.getRoles().addAll(roleRepository.findRolesByUserId(Long.valueOf(user.getId())));

        // Generate tokens using the JWT provider port
        List<Role> rolesList = new ArrayList<>(user.getRoles());
        String accessToken = jwtTokenProvider.generateToken(
                user.getId(),
                user.getUsername(),
                rolesList
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return new JwtDto(accessToken, refreshToken);
    }
}