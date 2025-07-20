package com.harmoni.auth.http.handler;

import com.harmoni.auth.http.response.RestAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for unhandled internal server errors (HTTP 500).
 *
 * <p>This class catches all uncaught exceptions and formats them
 * into a standard {@link RestAPIResponse} with status 500 (Internal Server Error).</p>
 */
@RestControllerAdvice
@Slf4j
public class InternalServerExceptionHandler {

    /**
     * Handles generic {@link Exception} instances not explicitly handled elsewhere.
     *
     * @param exception the unhandled exception
     * @return a {@link ResponseEntity} with HTTP 500 status and a generic error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestAPIResponse> globalExceptionHandler(Exception exception) {
        log.error("Internal Server Error: {}", exception.getMessage(), exception);

        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timeStamp(System.currentTimeMillis())
                .error(HttpStatus.INTERNAL_SERVER_ERROR)
                .data(null)
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
