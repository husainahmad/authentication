package com.harmoni.auth.web.handler;

import com.harmoni.auth.application.exception.BusinessNotFoundRequestException;
import com.harmoni.auth.web.dto.CommonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * Global exception handler for business-level "not found" exceptions.
 *
 * <p>This handler catches {@link BusinessNotFoundRequestException} thrown when
 * a resource (e.g., user, store, tier) is not found and formats a proper API response
 * with a localized error message.</p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class NotFoundRequestExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Constructs the handler with injected {@link MessageSource} to support i18n error messages.
     *
     * @param messageSource the source used for resolving error messages based on locale
     */
    @Autowired
    public NotFoundRequestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Handles {@link BusinessNotFoundRequestException} and returns a consistent API response.
     *
     * @param e      the thrown exception
     * @param locale the client's locale for message translation
     * @return a {@link ResponseEntity} with a localized error message and HTTP 400 status
     */
    @ExceptionHandler(BusinessNotFoundRequestException.class)
    public ResponseEntity<CommonDto> badRequestExceptionHandler(
            BusinessNotFoundRequestException e, Locale locale) {

        String messageName = e.getMessage();
        Object[] args = e.getArgs();

        String message = messageSource.getMessage(messageName, args, locale);
        log.warn("NotFoundRequest: {}", message);

        CommonDto restAPIResponse = CommonDto.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .timeStamp(System.currentTimeMillis())
                .data(null)
                .error(message)
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.BAD_REQUEST);
    }
}
