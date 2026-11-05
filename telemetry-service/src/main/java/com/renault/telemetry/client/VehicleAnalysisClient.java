package com.renault.telemetry.client;

import com.renault.telemetry.dto.resquest.AnalysisRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "vehicle-analysis-service",
        url = "${analysis.service.url}"
)
public interface VehicleAnalysisClient {

    @PostMapping("/internal/analyze")
    void analyze(@RequestBody AnalysisRequest request);
}
