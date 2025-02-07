package com.harmoni.auth.service;

import com.harmoni.auth.component.JwtUtil;
import com.harmoni.auth.exception.BusinessUnAuthorizedRequestException;
import com.harmoni.auth.mapper.UserMapper;
import com.harmoni.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public String authenticate(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessUnAuthorizedRequestException("exception.auth.username.password.notFound", null);
        }
        return jwtUtil.generateToken(username);
    }

}
