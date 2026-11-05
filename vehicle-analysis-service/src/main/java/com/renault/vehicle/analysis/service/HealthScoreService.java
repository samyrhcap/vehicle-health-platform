package com.renault.vehicle.analysis.service;

import com.renault.vehicle.analysis.dto.request.AnalysisRequest;

public interface HealthScoreService {

    int calculateHealthScore(
            AnalysisRequest request,
            boolean activeRecall
    );
}