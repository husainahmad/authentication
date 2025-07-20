package com.harmoni.auth.http.handler;

import com.harmoni.auth.http.response.RestAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for request validation errors.
 * <p>
 * Captures {@link MethodArgumentNotValidException} and responds with
 * a 400 BAD_REQUEST containing a list of validation messages.
 * </p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ValidationExceptionHandler {

    /**
     * Handles validation exceptions triggered by @Valid annotated request bodies.
     *
     * @param e MethodArgumentNotValidException thrown by Spring during validation
     * @return ResponseEntity with structured error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestAPIResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.add(fieldError.getField() + " > " + fieldError.getDefaultMessage())
        );

        e.getBindingResult().getGlobalErrors().forEach(objectError ->
                errors.add(objectError.getObjectName() + " > " + objectError.getDefaultMessage())
        );

        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .timeStamp(System.currentTimeMillis())
                .data(null)
                .error(errors)
                .build();

        log.warn("Validation failed: {}", errors);

        return new ResponseEntity<>(restAPIResponse, HttpStatus.BAD_REQUEST);
    }
}
