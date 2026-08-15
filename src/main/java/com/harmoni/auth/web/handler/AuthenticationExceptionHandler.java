package com.harmoni.auth.web.handler;

import com.harmoni.auth.application.exception.AuthenticationException;
import com.harmoni.auth.web.dto.CommonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for authentication failures.
 * <p>
 * Captures {@link AuthenticationException} (e.g. user not found or invalid credentials)
 * and provides a structured 401 UNAUTHORIZED response instead of a generic 500 error.
 * </p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class AuthenticationExceptionHandler {

    /**
     * Handles {@link AuthenticationException} and returns a 401 response.
     *
     * @param e the authentication failure exception
     * @return a response entity with 401 status
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonDto> handleAuthenticationException(AuthenticationException e) {
        String message = e.getMessage();

        CommonDto restAPIResponse = CommonDto.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .timeStamp(System.currentTimeMillis())
                .error(message)
                .data(null)
                .build();

        logAsUnauthorized(message);
        return new ResponseEntity<>(restAPIResponse, HttpStatus.UNAUTHORIZED);
    }

    private static void logAsUnauthorized(String message) {
        log.warn("Authentication failed: {}", message);
    }
}