package com.renault.vehicle.analysis.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RecallResponse",
        description = "Response returned by the external recall provider"
)
public record RecallResponse(

        @Schema(
                description = "Indicates whether an active recall campaign exists for the vehicle",
                example = "true"
        )
        boolean activeRecall

) {
}