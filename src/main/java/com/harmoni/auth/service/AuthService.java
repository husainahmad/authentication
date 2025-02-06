package com.harmoni.auth.service;

import com.harmoni.auth.component.JwtUtil;
import com.harmoni.auth.mapper.UserMapper;
import com.harmoni.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public Mono<String> authenticate(String username, String password) {
        User user = userMapper.selectByUsernamePassword(username, password);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return Mono.error(new RuntimeException("Invalid credentials"));
        }
        return Mono.just(jwtUtil.generateToken(username));
    }

}
