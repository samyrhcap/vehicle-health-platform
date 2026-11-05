package com.renault.vehicle.analysis;

import com.renault.vehicle.analysis.dao.entity.Alert;
import com.renault.vehicle.analysis.dao.entity.HealthReport;
import com.renault.vehicle.analysis.dao.repository.AlertRepository;
import com.renault.vehicle.analysis.dao.repository.HealthReportRepository;
import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.enums.AlertType;
import com.renault.vehicle.analysis.enums.VehicleHealthStatus;
import com.renault.vehicle.analysis.service.AnalysisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Analysis Service Integration Tests")
class AnalysisServiceIntegrationTest {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private HealthReportRepository healthReportRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Test
    @DisplayName("Should analyze vehicle data and persist health report and alerts")
    void shouldAnalyzeVehicleAndPersistResults() {

        // GIVEN

        alertRepository.deleteAll();
        healthReportRepository.deleteAll();

        String vin = "VF1RFA12345678901";

        AnalysisRequest request =
                new AnalysisRequest(
                        vin,
                        150000,
                        15,
                        5,
                        1.7,
                        110.0,
                        List.of(
                                "P001",
                                "P002"
                        )
                );

        // WHEN

        analysisService.analyze(request);

        // THEN

        assertThat(
                healthReportRepository.count()
        ).isEqualTo(1);

        HealthReport report =
                healthReportRepository
                        .findAll()
                        .getFirst();

        assertThat(
                report.getVin()
        ).isEqualTo(vin);

        assertThat(
                report.getHealthScore()
        ).isEqualTo(20);

        assertThat(
                report.getStatus()
        ).isEqualTo(
                VehicleHealthStatus.CRITICAL
        );

        List<Alert> alerts =
                alertRepository.findAllByVin(
                        vin
                );

        assertThat(
                alerts
        ).hasSize(6);

        assertThat(
                alerts.stream()
                        .map(Alert::getType)
                        .toList()
        ).containsExactlyInAnyOrder(
                AlertType.LOW_BATTERY,
                AlertType.LOW_FUEL,
                AlertType.LOW_TIRE_PRESSURE,
                AlertType.ENGINE_OVERHEATING,
                AlertType.ERROR_CODES_DETECTED,
                AlertType.VEHICLE_RECALL
        );
    }
}