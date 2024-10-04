package com.example.smsSpringTest.exception.api;

import com.example.smsSpringTest.exception.ApiException;

public class FoundException extends ApiException {
    private static final long serialVersionUID = 1L;

    public FoundException(final String rspCode, final Object... args) {
        super(rspCode, args);
    }
}