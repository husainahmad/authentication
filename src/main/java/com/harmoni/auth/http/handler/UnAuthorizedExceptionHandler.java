package com.harmoni.auth.http.handler;

import com.harmoni.auth.exception.BusinessUnAuthorizedRequestException;
import com.harmoni.auth.http.response.RestAPIResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * Global exception handler for unauthorized access.
 * <p>
 * Captures {@link BusinessUnAuthorizedRequestException} and provides
 * a structured 401 UNAUTHORIZED response with a localized message.
 * </p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class UnAuthorizedExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handles {@link BusinessUnAuthorizedRequestException} and returns a localized response.
     *
     * @param e      the unauthorized access exception
     * @param locale the client locale for message translation
     * @return a response entity with 401 status
     */
    @ExceptionHandler(BusinessUnAuthorizedRequestException.class)
    public ResponseEntity<RestAPIResponse> handleUnAuthorizedException(BusinessUnAuthorizedRequestException e, Locale locale) {
        String messageName = e.getMessage();
        Object[] args = e.getArgs();
        String message = messageSource.getMessage(messageName, args, locale);

        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .timeStamp(System.currentTimeMillis())
                .error(message)
                .data(null)
                .build();

        logAsUnauthorized(message);
        return new ResponseEntity<>(restAPIResponse, HttpStatus.UNAUTHORIZED);
    }

    private static void logAsUnauthorized(String message) {
        log.warn("Unauthorized access: {}", message);
    }
}
