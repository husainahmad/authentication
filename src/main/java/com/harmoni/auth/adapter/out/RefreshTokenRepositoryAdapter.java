package com.harmoni.auth.adapter.out;

import com.harmoni.auth.domain.model.RefreshToken;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RefreshTokenRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Adapter implementing the RefreshTokenRepository port.
 * This is a secondary adapter (driven adapter) that connects the domain layer
 * to the existing MyBatis data access layer.
 */
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        refreshTokenMapper.insert(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken findById(Long id) {
        return refreshTokenMapper.selectByPrimaryKey(id);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenMapper.selectByToken(token);
    }

    @Override
    public RefreshToken findByUsername(String username) {
        return refreshTokenMapper.selectByUsername(username);
    }

    @Override
    public boolean deleteById(Long id) {
        return refreshTokenMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteByToken(String token) {
        return refreshTokenMapper.deleteByToken(token) > 0;
    }

    @Override
    public int deleteExpired(Date date) {
        return refreshTokenMapper.deleteExpired(date);
    }
}
