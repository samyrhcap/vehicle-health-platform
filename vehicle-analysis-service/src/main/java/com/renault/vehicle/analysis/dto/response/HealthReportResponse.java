package com.renault.vehicle.analysis.dto.response;

import com.renault.vehicle.analysis.enums.VehicleHealthStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(
        name = "HealthReportResponse",
        description = "Vehicle health analysis report"
)
public record HealthReportResponse(

        @Schema(
                description = "Vehicle Identification Number",
                example = "VF1RFA12345678901"
        )
        String vin,

        @Schema(
                description = "Calculated vehicle health score",
                example = "20"
        )
        Integer healthScore,

        @Schema(
                description = "Vehicle health status",
                example = "CRITICAL"
        )
        VehicleHealthStatus status,

        @Schema(
                description = "Date and time when analysis was performed",
                example = "2026-11-05T01:30:00"
        )
        LocalDateTime analyzedAt

) {
}