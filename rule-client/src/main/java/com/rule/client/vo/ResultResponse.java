package com.rule.client.vo;

import com.rule.client.exception.BaoShineWebErrorCause;
import com.rule.client.exception.ErrorCause;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse<T> implements Serializable {

    private static final long serialVersionUID = -6095013107539731687L;

    private int code;

    private String message;

    private T data;

    public ResultResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<>(BaoShineWebErrorCause.SUCCESS.getCode(), BaoShineWebErrorCause.SUCCESS.getMessage(), data);
    }

    public static <T> ResultResponse<T> failure(ErrorCause errorCause) {
        return new ResultResponse<>(errorCause.getCode(), errorCause.getMessage(), null);
    }

    public static <T> ResultResponse<T> failure(ErrorCause errorCause, String errorMessage) {
        return new ResultResponse<>(errorCause.getCode(), errorMessage);
    }

}