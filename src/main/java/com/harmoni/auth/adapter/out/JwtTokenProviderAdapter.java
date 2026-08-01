package com.harmoni.auth.adapter.out;

import com.harmoni.auth.application.port.out.JwtTokenProvider;
import com.harmoni.auth.component.JwtUtil;
import com.harmoni.auth.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adapter implementing the JwtTokenProvider port.
 * This is a secondary adapter (driven adapter) that provides JWT token functionality
 * by wrapping the existing JwtUtil component.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProviderAdapter implements JwtTokenProvider {

    private final JwtUtil jwtUtil;

    @Override
    public String generateToken(Integer userId, String username, List<Role> roles) {
        return jwtUtil.generateToken(userId, username, roles);
    }

    @Override
    public String generateRefreshToken(String username) {
        return jwtUtil.generateRefreshToken(username);
    }

    @Override
    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    @Override
    public boolean validateToken(String token, String username) {
        return jwtUtil.validateToken(token, username);
    }
}
