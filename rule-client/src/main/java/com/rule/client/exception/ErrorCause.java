package com.rule.client.exception;

public interface ErrorCause {

    int getCode();

    String getMessage();

    int getStatusCode();
}
