package com.renault.vehicle.analysis.service.helpers;

import com.renault.vehicle.analysis.dto.request.AnalysisRequest;

public final class AlertRuleHelper {

    private AlertRuleHelper() {
    }

    public static boolean isLowBattery(AnalysisRequest request) {
        return request.batteryLevel() < 20;
    }

    public static boolean isLowFuel(AnalysisRequest request) {
        return request.fuelLevel() < 10;
    }

    public static boolean isLowTirePressure(AnalysisRequest request) {
        return request.tirePressure() < 1.8;
    }

    public static boolean isEngineOverheating(AnalysisRequest request) {
        return request.engineTemperature() > 100.0;
    }

    public static boolean hasErrorCodes(AnalysisRequest request) {
        return request.errorCodes() != null
                && !request.errorCodes().isEmpty();
    }

    public static boolean hasActiveRecall(boolean activeRecall) {
        return activeRecall;
    }
}