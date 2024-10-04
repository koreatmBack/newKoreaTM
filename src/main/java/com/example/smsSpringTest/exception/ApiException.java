package com.example.smsSpringTest.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String code;
    private final Object[] args;

    public ApiException(final String code, final Object... args) {
        this.code = code;
        this.args = args;
    }
}
