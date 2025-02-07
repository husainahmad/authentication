package com.harmoni.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = "{validation.auth.username.NotBlank}")
    private String username;
    @NotBlank(message = "{validation.auth.password.NotBlank}")
    private String password;
}
