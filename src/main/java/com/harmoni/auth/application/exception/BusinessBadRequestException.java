package com.harmoni.auth.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Custom exception indicating a business logic failure that results in a "400 Bad Request" response.
 * <p>
 * This exception is typically thrown when user input is invalid or violates a business rule.
 * It can carry additional arguments for use in localized or parameterized error messages.
 * </p>
 */
@Getter
@AllArgsConstructor
public class BusinessBadRequestException extends RuntimeException {

    /**
     * The message key or descriptive error message to be used in API responses or internationalization.
     */
    private final String message;

    /**
     * Optional arguments to support message parameterization or context.
     * This field is marked as {@code transient} to avoid serialization in some frameworks.
     */
    private final transient Object[] args;
}
