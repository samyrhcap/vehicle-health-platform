package com.renault.telemetry.web;

import com.renault.telemetry.dto.response.TelemetryResponse;
import com.renault.telemetry.dto.resquest.TelemetryRequest;
import com.renault.telemetry.service.TelemetryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/telemetry")
@Tag(
        name = "Telemetry",
        description = "Endpoints for receiving vehicle telemetry data"
)
public class TelemetryController {

    private final TelemetryService telemetryService;

    @Operation(
            summary = "Submit vehicle telemetry",
            description = """
                    Receives telemetry data from a connected vehicle,
                    stores it and triggers health analysis.
                    """
    )
    @ApiResponse(
            responseCode = "202",
            description = "Telemetry accepted and analysis triggered"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Validation error in telemetry payload"
    )
    @ApiResponse(
            responseCode = "502",
            description = "Vehicle analysis service unavailable"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Unexpected server error"
    )
    @PostMapping
    public ResponseEntity<TelemetryResponse> receiveTelemetry(
            @Valid @RequestBody TelemetryRequest request) {

        return ResponseEntity.accepted()
                .body(
                        telemetryService.receiveTelemetry(request)
                );
    }
}