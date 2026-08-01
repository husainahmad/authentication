package com.harmoni.auth.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Custom exception representing a business condition where the request was valid,
 * but no content is available to return (typically corresponds to HTTP 204 No Content).
 * <p>
 * Useful for signaling that the requested data exists in concept but is empty or irrelevant
 * in the current context (e.g. filtered results, optional data).
 * </p>
 */
@Getter
@AllArgsConstructor
public class BusinessNoContentRequestException extends RuntimeException {

    /**
     * The message key or description representing the no-content condition.
     */
    private final String message;

    /**
     * Optional arguments to support dynamic message formatting or context-specific data.
     * Marked as {@code transient} to prevent serialization when not needed.
     */
    private final transient Object[] args;
}
