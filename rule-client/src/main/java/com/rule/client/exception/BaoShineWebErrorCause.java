package com.rule.client.exception;

import org.springframework.http.HttpStatus;

public enum BaoShineWebErrorCause implements ErrorCause{

    SUCCESS(200, "success", HttpStatus.OK.value()),

    MISSING_REQUIRED_PARAMETERS(499, "missing required parameters", HttpStatus.BAD_REQUEST.value()),

    TOKEN_EXPRIED(498, "token is expried", HttpStatus.UNAUTHORIZED.value()),

    INVALID_PARAMETER(496, "invalid parameter", HttpStatus.BAD_REQUEST.value()),

    INVALID_STATUS(495, "invalid status", HttpStatus.UNPROCESSABLE_ENTITY.value()),

    MISSING_PARAMETER(494, "missing parameter", HttpStatus.BAD_REQUEST.value()),

    PRODUCT_CENTER_ERROR(599, "product center error", HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final int code;

    private final String message;

    private final int statusCode;

    BaoShineWebErrorCause(int code, String message, int statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}