package com.harmoni.auth.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Adapter implementing the PasswordEncoder port.
 * This is a secondary adapter (driven adapter) that provides password encoding
 * functionality by wrapping the Spring Security PasswordEncoder.
 */
@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements com.harmoni.auth.application.port.out.PasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
