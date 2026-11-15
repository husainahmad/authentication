package com.harmoni.auth.adapter.in;

import com.harmoni.auth.application.port.in.AuthoritiesQueryUseCase;
import com.harmoni.auth.web.dto.AuthoritiesDto;
import com.harmoni.auth.web.dto.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing the effective authorities of the signed-in user.
 */
@RestController
@RequestMapping("/api/v1/auth/me")
@RequiredArgsConstructor
public class AuthoritiesController {

    private final AuthoritiesQueryUseCase authoritiesQueryUseCase;

    @GetMapping("/authorities")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonDto> authorities(Authentication authentication) {
        AuthoritiesDto authorities = authoritiesQueryUseCase.findByUsername(authentication.getName());
        return ResponseEntity.ok(CommonDto.builder()
                .httpStatus(HttpStatus.OK.value())
                .data(authorities)
                .build());
    }
}
