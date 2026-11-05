package com.renault.vehicle.analysis.web;

import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.service.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
@Tag(
        name = "Internal Analysis",
        description = "Internal endpoints used by telemetry-service"
)
public class InternalAnalysisController {

    private final AnalysisService analysisService;

    @Operation(
            summary = "Analyze vehicle telemetry",
            description = """
                    Receives telemetry data from telemetry-service,
                    calculates the vehicle health score,
                    generates alerts and stores the analysis result.
                    """
    )
    @ApiResponse(
            responseCode = "202",
            description = "Analysis accepted successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid analysis request"
    )
    @ApiResponse(
            responseCode = "502",
            description = "Recall provider unavailable"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Unexpected server error"
    )
    @PostMapping("/analyze")
    public ResponseEntity<Void> analyze(
            @Valid @RequestBody AnalysisRequest request) {

        analysisService.analyze(request);

        return ResponseEntity.accepted()
                .build();
    }
}