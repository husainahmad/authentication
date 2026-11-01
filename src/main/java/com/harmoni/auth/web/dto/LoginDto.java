package com.harmoni.auth.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotBlank(message = "{validation.auth.username.NotBlank}")
    private String username;
    @NotBlank(message = "{validation.auth.password.NotBlank}")
    private String password;
}
