package com.harmoni.auth.model.dto;

import com.harmoni.auth.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "{validation.user.username.NotBlank}")
    @Size(min = 6, max = 125)
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "{validation.user.username.pattern.NotValid}")
    private String username;
    @NotBlank(message = "{validation.user.password.NotBlank}")
    @Size(min = 6, max = 125)
    private String password;
    @NotBlank(message = "{validation.user.email.NotBlank}")
    @Size(min = 2, max = 125)
    @Email(message = "{validation.user.email.NotValid}")
    private String email;
    public User toUser() {
        return new User().setUsername(username)
                .setPassword(password)
                .setEmail(email);
    }
}
