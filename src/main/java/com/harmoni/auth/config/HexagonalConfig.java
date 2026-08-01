package com.harmoni.auth.config;

import com.harmoni.auth.adapter.in.AuthController;
import com.harmoni.auth.adapter.out.JwtTokenProviderAdapter;
import com.harmoni.auth.adapter.out.PermissionRepositoryAdapter;
import com.harmoni.auth.adapter.out.RefreshTokenRepositoryAdapter;
import com.harmoni.auth.adapter.out.RoleRepositoryAdapter;
import com.harmoni.auth.adapter.out.UserRepositoryAdapter;
import com.harmoni.auth.application.port.out.JwtTokenProvider;
import com.harmoni.auth.application.port.out.PasswordEncoder;
import com.harmoni.auth.application.usecase.AuthenticateUserUseCaseImpl;
import com.harmoni.auth.application.usecase.RefreshTokenUseCaseImpl;
import com.harmoni.auth.application.usecase.UserManagementUseCaseImpl;
import com.harmoni.auth.component.JwtUtil;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.PermissionMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RefreshTokenMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RoleMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.UserMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RefreshTokenRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RoleRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for hexagonal architecture adapters and beans.
 * This configures the adapters that connect the hexagonal architecture layers
 * and the infrastructure beans.
 */
@Configuration
public class HexagonalConfig {

    @Bean
    public AuthController authController(
            AuthenticateUserUseCaseImpl authenticateUserUseCase,
            RefreshTokenUseCaseImpl refreshTokenUseCase) {
        return new AuthController(authenticateUserUseCase, refreshTokenUseCase);
    }

    @Bean
    public JwtTokenProviderAdapter jwtTokenProviderAdapter(JwtUtil jwtUtil) {
        return new JwtTokenProviderAdapter(jwtUtil);
    }

    @Bean
    public PermissionRepositoryAdapter permissionRepositoryAdapter(PermissionMapper permissionMapper) {
        return new PermissionRepositoryAdapter(permissionMapper);
    }

    @Bean
    public RefreshTokenRepositoryAdapter refreshTokenRepositoryAdapter(RefreshTokenMapper refreshTokenMapper) {
        return new RefreshTokenRepositoryAdapter(refreshTokenMapper);
    }

    @Bean
    public RoleRepositoryAdapter roleRepositoryAdapter(RoleMapper roleMapper) {
        return new RoleRepositoryAdapter(roleMapper);
    }

    @Bean
    public UserRepositoryAdapter userRepositoryAdapter(UserMapper userMapper) {
        return new UserRepositoryAdapter(userMapper);
    }

    @Bean
    public AuthenticateUserUseCaseImpl authenticateUserUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder) {
        return new AuthenticateUserUseCaseImpl(
                userRepository, roleRepository,
                jwtTokenProvider, passwordEncoder);
    }

    @Bean
    public RefreshTokenUseCaseImpl refreshTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider) {
        return new RefreshTokenUseCaseImpl(
                refreshTokenRepository, userRepository, jwtTokenProvider);
    }

    @Bean
    public UserManagementUseCaseImpl userManagementUseCase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return new UserManagementUseCaseImpl(userRepository, passwordEncoder);
    }


}
