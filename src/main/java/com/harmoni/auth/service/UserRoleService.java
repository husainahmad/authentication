package com.harmoni.auth.service;

import com.harmoni.auth.mapper.UserRoleMapper;
import com.harmoni.auth.model.UserRoleKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userRoleService")
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleMapper userRoleMapper;

    public List<UserRoleKey> selectRolesByUserId(Integer userId) {
        return userRoleMapper.selectRolesByUserId(userId);
    }
}
