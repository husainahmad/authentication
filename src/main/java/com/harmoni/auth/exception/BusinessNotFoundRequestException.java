package com.harmoni.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessNotFoundRequestException extends RuntimeException {
    private final String message;
    private final transient Object[] args;
}
