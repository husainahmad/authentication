package com.harmoni.auth.application.exception;


/**
 * Application-level exception for authentication-related errors.
 * This exception is thrown by application services when authentication fails.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}