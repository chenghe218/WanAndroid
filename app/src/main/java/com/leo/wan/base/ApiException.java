package com.leo.wan.base;

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/7/31 11:12
 */
public class ApiException extends RuntimeException {
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}