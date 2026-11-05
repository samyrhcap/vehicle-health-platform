package com.renault.vehicle.analysis.exception;

import com.renault.vehicle.analysis.enums.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String details;

    protected BusinessException(
            ErrorCode errorCode,
            String details) {

        super(errorCode.getMessage());

        this.errorCode = errorCode;
        this.details = details;
    }
}