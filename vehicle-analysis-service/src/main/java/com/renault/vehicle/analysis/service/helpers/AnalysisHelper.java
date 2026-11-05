package com.renault.vehicle.analysis.service.helpers;

import com.renault.vehicle.analysis.dao.entity.Alert;
import com.renault.vehicle.analysis.dao.entity.HealthReport;
import com.renault.vehicle.analysis.dao.repository.AlertRepository;
import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.enums.AlertType;
import com.renault.vehicle.analysis.enums.VehicleHealthStatus;

import java.util.List;

public final class AnalysisHelper {

    private static final int EXCELLENT_THRESHOLD = 80;
    private static final int GOOD_THRESHOLD = 60;
    private static final int WARNING_THRESHOLD = 40;

    private AnalysisHelper() {
    }

    public static VehicleHealthStatus determineStatus(int score) {

        if (score >= EXCELLENT_THRESHOLD) {
            return VehicleHealthStatus.EXCELLENT;
        }

        if (score >= GOOD_THRESHOLD) {
            return VehicleHealthStatus.GOOD;
        }

        if (score >= WARNING_THRESHOLD) {
            return VehicleHealthStatus.WARNING;
        }

        return VehicleHealthStatus.CRITICAL;
    }

    public static HealthReport buildHealthReport(
            AnalysisRequest request,
            int healthScore,
            VehicleHealthStatus status) {

        return HealthReport.builder()
                .vin(request.vin())
                .healthScore(healthScore)
                .status(status)
                .build();
    }

    public static void persistAlerts(
            String vin,
            List<AlertType> alertTypes,
            AlertRepository alertRepository) {

        List<Alert> alerts =
                alertTypes.stream()
                        .map(alertType ->
                                Alert.builder()
                                        .vin(vin)
                                        .type(alertType)
                                        .message(alertType.name())
                                        .build()
                        )
                        .toList();

        alertRepository.saveAll(alerts);
    }
}