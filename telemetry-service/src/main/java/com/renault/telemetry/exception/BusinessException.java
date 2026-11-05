package com.renault.telemetry.exception;

import com.renault.telemetry.enums.ApiErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ApiErrorCode errorCode;

    public BusinessException(ApiErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
