package com.harmoni.auth.application.port.in;

import com.harmoni.auth.web.dto.AuthoritiesDto;

/**
 * Input port for reading the effective authorities of a user.
 */
public interface AuthoritiesQueryUseCase {

    AuthoritiesDto findByUserId(Long userId);

    AuthoritiesDto findByUsername(String username);
}
