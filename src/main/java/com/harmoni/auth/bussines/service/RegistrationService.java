package com.harmoni.auth.bussines.service;

import com.harmoni.auth.exception.BusinessNotFoundRequestException;
import com.harmoni.auth.model.User;
import com.harmoni.auth.model.dto.UserDto;
import com.harmoni.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public int register(UserDto userDto) {
        if (userService.selectByUsername(userDto.getUsername())!=null) {
            throw new BusinessNotFoundRequestException("exception.user.username.exist", null);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.create(userDto.toUser());
    }

    public int deactivate(String username) {
        User user = userService.selectByUsername(username);
        if (user==null) {
            throw new BusinessNotFoundRequestException("exception.user.username.notFound", null);
        }
        return userService.deleteByUsername(user);
    }

    public int update(UserDto userDto) {
        User user = userService.selectByUsername(userDto.getUsername());
        if (user==null) {
            throw new BusinessNotFoundRequestException("exception.user.username.notFound", null);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.updateByUsername(userDto.toUser());
    }

}
