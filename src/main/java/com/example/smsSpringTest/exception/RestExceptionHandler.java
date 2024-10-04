package com.example.smsSpringTest.exception;

import com.example.smsSpringTest.exception.api.*;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestExceptionHandler {

    private static final String NOT_MODIFIED_STRING = "UP_TO_DATE";

    private final MessageSource messageSource;

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleBadRequestException(final ApiException e, final Locale locale) {
        return returnApiResponse(e, locale);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleIllegalArgumentException(final IllegalArgumentException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E400, locale);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E002, locale);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleBadCredentialsException(final BadCredentialsException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E005, locale);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleNullPointerException(final NullPointerException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E400, locale);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleHttpMessageNotReadableException(final HttpMessageNotReadableException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E006, locale);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleNoSuchAlgorithmException(final NoSuchAlgorithmException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E007, locale);
    }

    @ExceptionHandler(NoSuchPaddingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleNoSuchPaddingException(final NoSuchPaddingException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E007, locale);
    }
    @ExceptionHandler(InvalidAlgorithmParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleInvalidAlgorithmParameterException(final InvalidAlgorithmParameterException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E009, locale);
    }
    @ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleUnsupportedEncodingException(final BadCredentialsException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E010, locale);
    }
    @ExceptionHandler(IllegalBlockSizeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleIllegalBlockSizeException(final IllegalBlockSizeException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E011, locale);
    }
    @ExceptionHandler(BadPaddingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleBadPaddingException(final BadPaddingException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E012, locale);
    }
    @ExceptionHandler(InvalidKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleInvalidKeyException(final InvalidKeyException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E013, locale);
    }
    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleJsonProcessingException(final JsonProcessingException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E014, locale);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse handleConflictException(final ConflictException e, final Locale locale) {
        return returnApiResponse(e, locale);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleMaxUploadSizeExceededException(final MaxUploadSizeExceededException e, final Locale locale) {
        return returnApiResponse(e, locale);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse handleDuplicateKeyException(final DuplicateKeyException e, final Locale locale) {
        log.error("DuplicateKeyException caught.", e);
        return returnApiResponse(ErrorCode.E409, locale);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse handleForbiddenException(final ApiException e, final Locale locale) {
        return returnApiResponse(e, locale);
    }

    @ExceptionHandler(FoundException.class)
    @ResponseStatus(HttpStatus.FOUND)
    public ApiResponse handleFoundException(final ApiException e, final Locale locale) {
        return returnApiResponse(e, locale);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleInternalServerErrorException(final InternalServerErrorException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E504, locale);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleMissingRequestHeaderException(final MissingRequestHeaderException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E402, locale);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException e, final Locale locale) {
        return returnApiResponse(ErrorCode.E401, locale);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleNotFoundException(final ApiException e, final Locale locale) {
        return returnApiResponse(e, locale);
    }

    @ExceptionHandler(NotModifiedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse handleNotModifiedException(final ApiException e, final Locale locale) {
        return new ApiResponse(e.getCode(), NOT_MODIFIED_STRING);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiResponse handleServiceUnavailableException(final ApiException e, final Locale locale) {
        return returnApiResponse(e, locale);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleThrowable(final Throwable t, final Locale locale) {
        return returnApiResponse(ErrorCode.E504, locale);
    }

    @ExceptionHandler(TooManyRequestException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiResponse handleTooManyRequestException(final ApiException e, final Locale locale) {
        return returnApiResponse(e, locale);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleUnauthorizedException(final ApiException e, final Locale locale) {
        return returnApiResponse(e, locale);
    }

    private ApiResponse returnApiResponse(final ApiException e, final Locale locale) {
        final String responseMessage = messageSource.getMessage(e.getCode(), e.getArgs(), locale);
        log.error(responseMessage, e);
        e.printStackTrace();

        return new ApiResponse(e.getCode(), responseMessage);
    }

    private ApiResponse returnApiResponse(final ErrorCode errorCode, final Locale locale, final Object... args) {
        final String responseMessage = messageSource.getMessage(errorCode.getCode(), args, locale);

        return new ApiResponse(errorCode.getCode(), responseMessage);
    }

}
