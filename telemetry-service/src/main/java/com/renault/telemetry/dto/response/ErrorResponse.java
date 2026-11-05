package com.renault.telemetry.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Standard API error response"
)
public record ErrorResponse(

        @Schema(
                description = "Unique application error code",
                example = "TEL_004"
        )
        String code,

        @Schema(
                description = "Human-readable error message",
                example = "VIN is mandatory"
        )
        String message,

        @Schema(
                description = "HTTP status code",
                example = "400"
        )
        int status,

        @Schema(
                description = "API endpoint path",
                example = "/api/v1/telemetry"
        )
        String path,

        @Schema(
                description = "Error creation timestamp in UTC",
                example = "2026-11-04T11:15:42"
        )
        LocalDateTime timestamp

) {
}