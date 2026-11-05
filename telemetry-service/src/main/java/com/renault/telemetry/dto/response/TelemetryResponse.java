package com.renault.telemetry.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(
        name = "TelemetryResponse",
        description = "Response returned when telemetry data is successfully accepted"
)
public record TelemetryResponse(

        @Schema(
                description = "Vehicle Identification Number",
                example = "VF1RFA12345678901"
        )
        String vin,

        @Schema(
                description = "Telemetry processing status",
                example = "ACCEPTED"
        )
        String status,

        @Schema(
                description = "Telemetry reception timestamp in UTC",
                example = "2026-11-04T11:15:42"
        )
        LocalDateTime receivedAt
) {
}