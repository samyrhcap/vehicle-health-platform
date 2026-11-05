package com.renault.vehicle.analysis.web;

import com.renault.vehicle.analysis.dto.response.AlertResponse;
import com.renault.vehicle.analysis.dto.response.HealthReportResponse;
import com.renault.vehicle.analysis.service.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
@Tag(
        name = "Vehicle Analysis",
        description = "Vehicle health reports and alerts"
)
public class VehicleAnalysisController {

    private final AnalysisService analysisService;

    @Operation(
            summary = "Get vehicle health report",
            description = "Returns the latest health report for a vehicle"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Health report retrieved successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Health report not found"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Unexpected server error"
    )
    @GetMapping("/{vin}/health-report")
    public ResponseEntity<HealthReportResponse> getHealthReport(

            @Parameter(
                    description = "Vehicle Identification Number",
                    example = "VF1RFA12345678901"
            )
            @PathVariable String vin) {

        return ResponseEntity.ok(
                analysisService.getHealthReport(vin)
        );
    }

    @Operation(
            summary = "Get vehicle alerts",
            description = "Returns all alerts generated for a vehicle"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Alerts retrieved successfully"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Unexpected server error"
    )
    @GetMapping("/{vin}/alerts")
    public ResponseEntity<List<AlertResponse>> getAlerts(

            @Parameter(
                    description = "Vehicle Identification Number",
                    example = "VF1RFA12345678901"
            )
            @PathVariable String vin) {

        return ResponseEntity.ok(
                analysisService.getAlerts(vin)
        );
    }
}
