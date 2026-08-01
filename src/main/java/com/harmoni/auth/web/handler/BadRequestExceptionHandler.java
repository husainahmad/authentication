package com.harmoni.auth.web.handler;

import com.harmoni.auth.application.exception.BusinessBadRequestException;
import com.harmoni.auth.web.dto.CommonDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * Handles HTTP 400 Bad Request exceptions and returns standardized error responses.
 *
 * <p>This class provides centralized exception handling for:
 * <ul>
 *   <li>{@link BusinessBadRequestException} - for domain-specific validation errors.</li>
 *   <li>{@link MissingServletRequestParameterException} - for missing required request parameters.</li>
 * </ul>
 * </p>
 *
 * <p>All responses are returned in a uniform {@link CommonDto} format,
 * including localized error messages where applicable.</p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class BadRequestExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handles {@link BusinessBadRequestException} by resolving the message
     * key to a localized error message and returning it in the response body.
     *
     * @param e      the business exception containing message key and arguments
     * @param locale the client locale used to localize the message
     * @return a {@link ResponseEntity} with HTTP 400 and error details
     */
    @ExceptionHandler(BusinessBadRequestException.class)
    public ResponseEntity<CommonDto> badRequestExceptionHandler(BusinessBadRequestException e, Locale locale) {
        String messageKey = e.getMessage();
        Object[] args = e.getArgs();
        String localizedMessage = messageSource.getMessage(messageKey, args, locale);

        CommonDto response = CommonDto.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .timeStamp(System.currentTimeMillis())
                .error(localizedMessage)
                .data(null)
                .build();

        logAsWarning(localizedMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link MissingServletRequestParameterException} by extracting the exception's message.
     *
     * @param e the exception triggered when a required parameter is not provided
     * @return a {@link ResponseEntity} with HTTP 400 and the error message
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonDto> missingRequiredParam(MissingServletRequestParameterException e) {
        String errorMessage = e.getMessage();

        CommonDto response = CommonDto.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .timeStamp(System.currentTimeMillis())
                .error(errorMessage)
                .data(null)
                .build();

        logAsWarning(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Logs a warning for bad request errors.
     *
     * @param message the warning message to be logged
     */
    private static void logAsWarning(String message) {
        log.warn("BadRequest: {}", message);
    }
}
