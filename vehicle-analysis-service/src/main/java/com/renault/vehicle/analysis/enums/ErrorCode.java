package com.renault.vehicle.analysis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    HEALTH_REPORT_NOT_FOUND(
            "ANL_001",
            HttpStatus.NOT_FOUND,
            "Health report not found"
    ),

    EXTERNAL_PROVIDER_UNAVAILABLE(
            "ANL_002",
            HttpStatus.BAD_GATEWAY,
            "External provider unavailable"
    ),

    VALIDATION_ERROR(
            "ANL_003",
            HttpStatus.BAD_REQUEST,
            "Validation error"
    ),

    INTERNAL_ERROR(
            "ANL_999",
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected error"
    );

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}