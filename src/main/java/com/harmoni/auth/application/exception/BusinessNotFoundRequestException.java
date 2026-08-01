package com.harmoni.auth.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception thrown when a requested resource or entity is not found.
 * <p>
 * Typically used to represent business-level 404 Not Found responses,
 * such as when a user, role, or data entry does not exist in the system.
 * </p>
 */
@Getter
@AllArgsConstructor
public class BusinessNotFoundRequestException extends RuntimeException {

    /**
     * The message key or human-readable description of the error.
     * This can be used directly in API responses or mapped via localization.
     */
    private final String message;

    /**
     * Additional arguments to support message formatting or context.
     * Marked as {@code transient} to avoid serialization unless needed.
     */
    private final transient Object[] args;
}
