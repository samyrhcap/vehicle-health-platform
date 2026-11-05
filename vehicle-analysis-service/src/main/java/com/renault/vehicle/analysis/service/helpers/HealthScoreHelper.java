package com.renault.vehicle.analysis.service.helpers;

import java.util.List;

public final class HealthScoreHelper {

    private HealthScoreHelper() {
    }

    public static int mileageImpact(Integer mileage) {

        if (mileage > 200_000) {
            return 20;
        }

        if (mileage > 100_000) {
            return 10;
        }

        return 0;
    }

    public static int batteryImpact(Integer batteryLevel) {
        return batteryLevel < 20 ? 20 : 0;
    }

    public static int fuelImpact(Integer fuelLevel) {
        return fuelLevel < 10 ? 5 : 0;
    }

    public static int tirePressureImpact(Double tirePressure) {
        return tirePressure < 1.8 ? 10 : 0;
    }

    public static int temperatureImpact(Double engineTemperature) {
        return engineTemperature > 100 ? 15 : 0;
    }

    public static int errorCodesImpact(List<String> errorCodes) {

        if (errorCodes == null || errorCodes.isEmpty()) {
            return 0;
        }

        return errorCodes.size() * 5;
    }

    public static int recallImpact(
            boolean activeRecall) {

        return activeRecall ? 10 : 0;
    }
}