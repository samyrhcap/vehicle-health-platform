package com.renault.vehicle.analysis.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Standard API error response"
)
public record ErrorResponse(

        @Schema(
                description = "Unique application error code",
                example = "ANL_001"
        )
        String code,

        @Schema(
                description = "Human-readable error message",
                example = "Health report not found"
        )
        String message,

        @Schema(
                description = "Additional error details",
                example = "No health report found for VIN VF1RFA12345678901"
        )
        String details,

        @Schema(
                description = "HTTP status code",
                example = "404"
        )
        int status,

        @Schema(
                description = "Requested API path",
                example = "/api/v1/vehicles/VF1RFA12345678901/health-report"
        )
        String path,

        @Schema(
                description = "Timestamp when the error occurred",
                example = "2026-11-05T00:45:00"
        )
        LocalDateTime timestamp

) {
}