package com.renault.vehicle.analysis.service;

import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.service.helpers.HealthScoreHelper;
import org.springframework.stereotype.Service;

@Service
public class HealthScoreServiceImpl implements HealthScoreService {

    private static final int INITIAL_SCORE = 100;

    @Override
    public int calculateHealthScore(
            AnalysisRequest request,
            boolean activeRecall) {

        int score = INITIAL_SCORE;

        score -= HealthScoreHelper.mileageImpact(
                request.mileage()
        );

        score -= HealthScoreHelper.batteryImpact(
                request.batteryLevel()
        );

        score -= HealthScoreHelper.fuelImpact(
                request.fuelLevel()
        );

        score -= HealthScoreHelper.tirePressureImpact(
                request.tirePressure()
        );

        score -= HealthScoreHelper.temperatureImpact(
                request.engineTemperature()
        );

        score -= HealthScoreHelper.errorCodesImpact(
                request.errorCodes()
        );

        score -= HealthScoreHelper.recallImpact(
                activeRecall
        );

        return Math.max(score, 0);
    }
}