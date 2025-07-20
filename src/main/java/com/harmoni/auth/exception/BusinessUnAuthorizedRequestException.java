package com.harmoni.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception thrown when a user attempts to perform an action without proper authentication or authorization.
 * <p>
 * This exception typically maps to an HTTP 401 Unauthorized response in API layers.
 * </p>
 */
@Getter
@AllArgsConstructor
public class BusinessUnAuthorizedRequestException extends RuntimeException {

    /**
     * A descriptive error message or message key to support localization or structured API responses.
     */
    private final String message;

    /**
     * Optional parameters for formatting the message or providing context.
     * Declared {@code transient} to avoid unnecessary serialization.
     */
    private final transient Object[] args;
}
