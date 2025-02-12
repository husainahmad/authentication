package com.harmoni.auth.service;

import com.harmoni.auth.mapper.UserMapper;
import com.harmoni.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("userService")
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public int create(User user) {
        return userMapper.insert(user);
    }

    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    public int deleteByUsername(User user) {
        return userMapper.deleteByUsername(user.getUsername());
    }

    public int updateByUsername(User user) {
        return userMapper.updateByPrimaryKey(user);
    }



}
