package com.example.smsSpringTest.exception;

import com.example.smsSpringTest.exception.api.*;

public interface ExceptionFactory {

    static ApiException getException(final ErrorCode errorCode) {
        switch (errorCode) {
            case E001:
                return new NotModifiedException(errorCode.getCode());
            case E401:
            case E402:
                return new BadRequestException(errorCode.getCode());
            case E403:
            case E404:
                return new UnauthorizedException(errorCode.getCode());
            case E406:
                return new EmptyFileException(errorCode.getCode());
            case E409:
                return new ConflictException(errorCode.getCode());
            case E501:
            case E502:
            case E503:
            case E504:
                return new InternalServerErrorException(errorCode.getCode());
            case E505:
                return new MaxUploadSizeExceededException(errorCode.getCode());
            default:
                return new InternalServerErrorException(ErrorCode.E504.getCode());
        }
    }
}
