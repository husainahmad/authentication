package com.harmoni.auth.application.usecase;

import com.harmoni.auth.web.dto.JwtDto;
import com.harmoni.auth.application.exception.AuthenticationException;
import com.harmoni.auth.application.port.in.RefreshTokenUseCase;
import com.harmoni.auth.application.port.out.JwtTokenProvider;
import com.harmoni.auth.domain.model.Role;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RefreshTokenRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Use case for refreshing an access token using a refresh token.
 * This application service orchestrates the token refresh process.
 * Implements the RefreshTokenUseCase input port.
 */
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenUseCaseImpl(RefreshTokenRepository refreshTokenRepository,
                                   UserRepository userRepository,
                                   JwtTokenProvider jwtTokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Refreshes an access token using the provided refresh token.
     *
     * @param refreshToken the refresh token to use for generating new tokens
     * @return an AuthResponseDto containing the new access and refresh tokens
     * @throws AuthenticationException if the refresh token is invalid or expired
     */
    @Override
    public JwtDto refresh(String refreshToken) {
        // Find the refresh token in the repository
        var tokenEntity = refreshTokenRepository.findByToken(refreshToken);
        if (tokenEntity == null) {
            throw new AuthenticationException("Invalid refresh token");
        }

        // Check if the token is expired
        if (tokenEntity.isExpired()) {
            throw new AuthenticationException("Expired refresh token");
        }

        // Find the user associated with the refresh token
        var user = userRepository.findById(tokenEntity.getUserId());
        if (user == null) {
            throw new AuthenticationException("User not found for refresh token");
        }

        // Generate new access and refresh tokens using the JWT provider port
        List<Role> rolesList = new ArrayList<>(user.getRoles());
        String newAccessToken = jwtTokenProvider.generateToken(
                user.getId(),
                user.getUsername(),
                rolesList
        );

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return new JwtDto(newAccessToken, newRefreshToken);
    }
}