package com.harmoni.auth.config;

import com.harmoni.auth.adapter.in.AuthController;
import com.harmoni.auth.adapter.out.JwtTokenProviderAdapter;
import com.harmoni.auth.adapter.out.PermissionRepositoryAdapter;
import com.harmoni.auth.adapter.out.RbacAuditRepositoryAdapter;
import com.harmoni.auth.adapter.out.RefreshTokenRepositoryAdapter;
import com.harmoni.auth.adapter.out.RolePermissionRepositoryAdapter;
import com.harmoni.auth.adapter.out.RoleRepositoryAdapter;
import com.harmoni.auth.adapter.out.UserRepositoryAdapter;
import com.harmoni.auth.adapter.out.UserRoleRepositoryAdapter;
import com.harmoni.auth.application.port.out.JwtTokenProvider;
import com.harmoni.auth.application.port.out.PasswordEncoder;
import com.harmoni.auth.application.usecase.AuthoritiesQueryUseCaseImpl;
import com.harmoni.auth.application.usecase.AuthenticateUserUseCaseImpl;
import com.harmoni.auth.application.usecase.PermissionManagementUseCaseImpl;
import com.harmoni.auth.application.usecase.RbacAuditRecorder;
import com.harmoni.auth.application.usecase.RefreshTokenUseCaseImpl;
import com.harmoni.auth.application.usecase.RoleManagementUseCaseImpl;
import com.harmoni.auth.application.usecase.UserManagementUseCaseImpl;
import com.harmoni.auth.application.usecase.UserRoleManagementUseCaseImpl;
import com.harmoni.auth.component.JwtUtil;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.PermissionMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RbacAuditMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RefreshTokenMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RoleMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RolePermissionMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.UserMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.UserRoleMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.PermissionRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RbacAuditRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RefreshTokenRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RolePermissionRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RoleRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRoleRepository;
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
    public UserRoleRepositoryAdapter userRoleRepositoryAdapter(UserRoleMapper userRoleMapper) {
        return new UserRoleRepositoryAdapter(userRoleMapper);
    }

    @Bean
    public RolePermissionRepositoryAdapter rolePermissionRepositoryAdapter(
            RolePermissionMapper rolePermissionMapper,
            PermissionMapper permissionMapper) {
        return new RolePermissionRepositoryAdapter(rolePermissionMapper, permissionMapper);
    }

    @Bean
    public AuthenticateUserUseCaseImpl authenticateUserUseCase(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            PermissionRepository permissionRepository,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder) {
        return new AuthenticateUserUseCaseImpl(
                userRepository, userRoleRepository, permissionRepository,
                jwtTokenProvider, passwordEncoder);
    }

    @Bean
    public RefreshTokenUseCaseImpl refreshTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            PermissionRepository permissionRepository,
            JwtTokenProvider jwtTokenProvider) {
        return new RefreshTokenUseCaseImpl(
                refreshTokenRepository, userRepository,
                userRoleRepository, permissionRepository,
                jwtTokenProvider);
    }

    @Bean
    public RbacAuditRepositoryAdapter rbacAuditRepositoryAdapter(RbacAuditMapper rbacAuditMapper) {
        return new RbacAuditRepositoryAdapter(rbacAuditMapper);
    }

    @Bean
    public RbacAuditRecorder rbacAuditRecorder(RbacAuditRepository rbacAuditRepository) {
        return new RbacAuditRecorder(rbacAuditRepository);
    }

    @Bean
    public RoleManagementUseCaseImpl roleManagementUseCase(
            RoleRepository roleRepository,
            RolePermissionRepository rolePermissionRepository,
            PermissionRepository permissionRepository,
            RbacAuditRecorder auditRecorder) {
        return new RoleManagementUseCaseImpl(
                roleRepository, rolePermissionRepository, permissionRepository, auditRecorder);
    }

    @Bean
    public PermissionManagementUseCaseImpl permissionManagementUseCase(
            PermissionRepository permissionRepository,
            RbacAuditRecorder auditRecorder) {
        return new PermissionManagementUseCaseImpl(permissionRepository, auditRecorder);
    }

    @Bean
    public UserRoleManagementUseCaseImpl userRoleManagementUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            RolePermissionRepository rolePermissionRepository,
            UserRoleRepository userRoleRepository,
            RbacAuditRecorder auditRecorder) {
        return new UserRoleManagementUseCaseImpl(
                userRepository, roleRepository, rolePermissionRepository, userRoleRepository, auditRecorder);
    }

    @Bean
    public AuthoritiesQueryUseCaseImpl authoritiesQueryUseCase(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            PermissionRepository permissionRepository) {
        return new AuthoritiesQueryUseCaseImpl(userRepository, userRoleRepository, permissionRepository);
    }

    @Bean
    public UserManagementUseCaseImpl userManagementUseCase(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository,
            RolePermissionRepository rolePermissionRepository,
            PasswordEncoder passwordEncoder) {
        return new UserManagementUseCaseImpl(
                userRepository, userRoleRepository, roleRepository,
                rolePermissionRepository, passwordEncoder);
    }


}
