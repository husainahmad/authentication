package com.harmoni.auth.bussines.service;

import com.harmoni.auth.component.JwtUtil;
import com.harmoni.auth.exception.BusinessUnAuthorizedRequestException;
import com.harmoni.auth.model.User;
import com.harmoni.auth.model.UserRoleKey;
import com.harmoni.auth.service.UserRoleService;
import com.harmoni.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRoleService userRoleService;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public String authenticate(String username, String password) {
        User user = userService.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessUnAuthorizedRequestException("exception.auth.username.password.notFound", null);
        }

        List<UserRoleKey> userRoles = userRoleService.selectRolesByUserId(user.getId());

        return jwtUtil.generateToken(username, userRoles);
    }

}
