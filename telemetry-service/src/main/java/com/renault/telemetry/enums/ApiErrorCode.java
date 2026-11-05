package com.renault.telemetry.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiErrorCode {


    ANALYSIS_SERVICE_UNAVAILABLE(
            "TEL_003",
            HttpStatus.BAD_GATEWAY,
            "Vehicle analysis service unavailable"
    ),

    VALIDATION_ERROR(
            "TEL_004",
            HttpStatus.BAD_REQUEST,
            "Validation error"
    ),

    INTERNAL_ERROR(
            "TEL_999",
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected error"
    );

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}