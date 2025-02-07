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

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class UnAuthorizedExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(BusinessUnAuthorizedRequestException.class)
    public ResponseEntity<RestAPIResponse>
        businessUnAuthorizedRequestException(BusinessUnAuthorizedRequestException e, Locale locale) {

        String messageName = e.getMessage();
        Object[] args = e.getArgs();

        String message = messageSource.getMessage(messageName, args, locale);

        RestAPIResponse restAPIResponse = RestAPIResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .timeStamp(System.currentTimeMillis())
                .error(message)
                .data(null)
                .build();

        logAsWarning(message);

        return new ResponseEntity<>(restAPIResponse, HttpStatus.UNAUTHORIZED);
    }

    private static void logAsWarning(String message) {
        log.warn("BadRequest: {}", message);
    }
}
