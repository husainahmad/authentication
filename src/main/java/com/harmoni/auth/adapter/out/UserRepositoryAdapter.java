package com.harmoni.auth.adapter.out;

import com.harmoni.auth.domain.model.User;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Adapter implementing the UserRepository port.
 * This is a secondary adapter (driven adapter) that connects the domain layer
 * to the existing MyBatis data access layer.
 *
 * Note: This adapter now works directly with the domain model User objects,
 * as the MyBatis mapper has been configured to map directly to/from the domain model.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        userMapper.insert(user);
        // The ID will be populated in the user object by MyBatis after insert
        return user;
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectByPrimaryKey(Math.toIntExact(id));
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public boolean deleteById(Long id) {
        User user = findById(id);
        if (user != null) {
            return userMapper.deleteByUsername(user.getUsername()) > 0;
        }
        return false;
    }
}