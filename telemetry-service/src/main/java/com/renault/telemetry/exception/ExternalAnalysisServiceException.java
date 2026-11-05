package com.renault.telemetry.exception;

import com.renault.telemetry.enums.ApiErrorCode;

public class ExternalAnalysisServiceException extends TelemetryException {

    public ExternalAnalysisServiceException() {

        super(ApiErrorCode.ANALYSIS_SERVICE_UNAVAILABLE);
    }
}