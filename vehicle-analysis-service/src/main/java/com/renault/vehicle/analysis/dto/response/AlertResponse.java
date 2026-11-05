package com.renault.vehicle.analysis.dto.response;

import com.renault.vehicle.analysis.enums.AlertType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AlertResponse",
        description = "Vehicle alert generated during health analysis"
)
public record AlertResponse(

        @Schema(
                description = "Alert type",
                example = "LOW_BATTERY"
        )
        AlertType type,

        @Schema(
                description = "Human-readable alert message",
                example = "Battery level is below the recommended threshold"
        )
        String message

) {
}