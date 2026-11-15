package com.harmoni.auth.web.handler;

import com.harmoni.auth.web.dto.CommonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps a denied {@code @PreAuthorize} check to HTTP 403 instead of a generic 500.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class AccessDeniedExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonDto> accessDenied(AccessDeniedException e) {
        log.warn("Access denied: {}", e.getMessage());
        CommonDto response = CommonDto.builder()
                .httpStatus(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
