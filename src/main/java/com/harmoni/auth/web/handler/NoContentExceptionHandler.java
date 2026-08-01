package com.harmoni.auth.web.handler;

import com.harmoni.auth.application.exception.BusinessNoContentRequestException;
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
 * Exception handler for business scenarios where no content is returned (HTTP 204).
 *
 * <p>This handler captures {@link BusinessNoContentRequestException} and formats it
 * into a consistent {@link CommonDto} with a 204 status code and localized error message.</p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class NoContentExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Constructor for injecting {@link MessageSource} to support internationalized messages.
     *
     * @param messageSource the message source used for resolving localized error messages
     */
    @Autowired
    public NoContentExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Handles {@link BusinessNoContentRequestException} and constructs a localized error response
     * with HTTP status 204 (No Content).
     *
     * @param e      the thrown business exception
     * @param locale the client's locale for message translation
     * @return a {@link ResponseEntity} containing a {@link CommonDto} with a 204 status code
     */
    @ExceptionHandler(BusinessNoContentRequestException.class)
    public ResponseEntity<CommonDto> badRequestExceptionHandler(
            BusinessNoContentRequestException e, Locale locale) {

        log.warn("NoContentRequest: ", e);

        String messageName = e.getMessage();
        Object[] args = e.getArgs();

        String message = messageSource.getMessage(messageName, args, locale);

        CommonDto restAPIResponse = CommonDto.builder()
                .httpStatus(HttpStatus.NO_CONTENT.value())
                .timeStamp(System.currentTimeMillis())
                .data(null)
                .error(message)
                .build();

        log.warn("NoContentRequest: {}", message);

        return new ResponseEntity<>(restAPIResponse, HttpStatus.NO_CONTENT);
    }
}
