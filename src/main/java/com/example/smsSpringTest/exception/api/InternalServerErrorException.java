package com.example.smsSpringTest.exception.api;

import com.example.smsSpringTest.exception.ApiException;

public class InternalServerErrorException extends ApiException {
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(final String rspCode, final Object... args) {
        super(rspCode, args);
    }
}