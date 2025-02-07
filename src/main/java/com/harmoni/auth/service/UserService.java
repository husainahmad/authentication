package com.harmoni.auth.service;

import com.harmoni.auth.component.JwtUtil;
import com.harmoni.auth.mapper.UserMapper;
import com.harmoni.auth.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public int create(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userMapper.insert(userDto.toUser());
    }

}
