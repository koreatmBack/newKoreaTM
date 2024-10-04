package com.example.smsSpringTest.exception.api;

import com.example.smsSpringTest.exception.ApiException;

public class ConflictException extends ApiException {
    private static final long serialVersionUID = 1L;

    public ConflictException(final String rspCode, final Object... args) {
        super(rspCode, args);
    }
}