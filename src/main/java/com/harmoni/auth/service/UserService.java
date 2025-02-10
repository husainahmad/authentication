package com.harmoni.auth.service;

import com.harmoni.auth.exception.BusinessNotFoundRequestException;
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
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessNotFoundRequestException("exception.user.username.notFound", null);
        }
        return user;
    }

}
