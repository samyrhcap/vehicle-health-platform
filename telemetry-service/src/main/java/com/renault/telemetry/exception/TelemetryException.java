package com.renault.telemetry.exception;


import com.renault.telemetry.enums.ApiErrorCode;

public class TelemetryException extends BusinessException {

    public TelemetryException(ApiErrorCode errorCode) {
        super(errorCode);
    }
}
