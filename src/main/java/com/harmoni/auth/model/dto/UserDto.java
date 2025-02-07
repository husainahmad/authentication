package com.harmoni.auth.model.dto;

import com.harmoni.auth.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "{validation.auth.username.NotBlank}")
    private String username;
    @NotBlank(message = "{validation.auth.password.NotBlank}")
    private String password;
    @NotBlank(message = "{validation.auth.email.NotBlank}")
    private String email;
    public User toUser() {
        return new User().setUsername(username)
                .setPassword(password)
                .setEmail(email);
    }
}
