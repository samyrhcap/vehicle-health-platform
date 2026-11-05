package com.renault.vehicle.analysis.service;

import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.dto.response.AlertResponse;
import com.renault.vehicle.analysis.dto.response.HealthReportResponse;
import java.util.List;

public interface AnalysisService {

    void analyze(AnalysisRequest request);
    HealthReportResponse getHealthReport(String vin);
    List<AlertResponse> getAlerts(String vin);
}