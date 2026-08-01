package com.harmoni.auth.web.handler;

import com.harmoni.auth.web.dto.CommonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * Global exception handler for malformed or unreadable HTTP message bodies.
 *
 * <p>This handler captures {@link HttpMessageNotReadableException}, which occurs when the request body
 * cannot be deserialized into a valid object (e.g., due to syntax errors, invalid JSON, etc.).
 * It returns a structured error response to the client.</p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class NotReadableExceptionHandler {

    /**
     * Handles {@link HttpMessageNotReadableException} and returns a standardized BAD_REQUEST response.
     *
     * @param e      the thrown exception indicating the body was not readable
     * @param locale the locale used for potential message localization (unused here)
     * @return a {@link ResponseEntity} with HTTP 400 and a default error message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonDto> notReadableExceptionHandler(HttpMessageNotReadableException e, Locale locale) {
        log.error("Malformed request body: {}", e.getMessage(), e);

        CommonDto restAPIResponse = CommonDto.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .timeStamp(System.currentTimeMillis())
                .error("Malformed or unreadable request body")
                .data(null)
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.BAD_REQUEST);
    }
}
