package com.renault.vehicle.analysis;

import com.renault.vehicle.analysis.client.RecallProviderClient;
import com.renault.vehicle.analysis.dao.entity.Alert;
import com.renault.vehicle.analysis.dao.entity.HealthReport;
import com.renault.vehicle.analysis.dao.repository.AlertRepository;
import com.renault.vehicle.analysis.dao.repository.HealthReportRepository;
import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.dto.response.AlertResponse;
import com.renault.vehicle.analysis.dto.response.HealthReportResponse;
import com.renault.vehicle.analysis.dto.response.RecallResponse;
import com.renault.vehicle.analysis.enums.AlertType;
import com.renault.vehicle.analysis.enums.VehicleHealthStatus;
import com.renault.vehicle.analysis.exception.ExternalProviderException;
import com.renault.vehicle.analysis.exception.HealthReportNotFoundException;
import com.renault.vehicle.analysis.mapper.AnalysisMapper;
import com.renault.vehicle.analysis.service.AlertService;
import com.renault.vehicle.analysis.service.AnalysisServiceImpl;
import com.renault.vehicle.analysis.service.HealthScoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Analysis Service Unit Tests")
class AnalysisServiceTest {

    private static final LocalDateTime ANALYZED_AT =
            LocalDateTime.of(
                    2026,
                    1,
                    1,
                    0,
                    0
            );

    @Mock
    private HealthScoreService healthScoreService;

    @Mock
    private AlertService alertService;

    @Mock
    private RecallProviderClient recallProviderClient;

    @Mock
    private AnalysisMapper analysisMapper;

    @Mock
    private HealthReportRepository healthReportRepository;

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AnalysisServiceImpl analysisService;

    @Test
    @DisplayName("Should analyze vehicle successfully and persist report and alerts")
    void shouldAnalyzeVehicleSuccessfully() {

        // GIVEN

        AnalysisRequest request =
                buildRequest();

        when(
                recallProviderClient.getRecallInformation(
                        request.vin()
                )
        ).thenReturn(
                new RecallResponse(true)
        );

        when(
                healthScoreService.calculateHealthScore(
                        request,
                        true
                )
        ).thenReturn(20);

        when(
                alertService.generateAlerts(
                        request,
                        true
                )
        ).thenReturn(
                List.of(
                        AlertType.LOW_BATTERY,
                        AlertType.VEHICLE_RECALL
                )
        );

        // WHEN

        analysisService.analyze(
                request
        );

        // THEN

        verify(recallProviderClient)
                .getRecallInformation(
                        request.vin()
                );

        verify(healthReportRepository)
                .save(any(HealthReport.class));

        verify(alertRepository)
                .saveAll(anyList());
    }

    @Test
    @DisplayName("Should throw ExternalProviderException when recall provider is unavailable")
    void shouldThrowExternalProviderException() {

        // GIVEN

        AnalysisRequest request =
                buildRequest();

        when(
                recallProviderClient.getRecallInformation(
                        request.vin()
                )
        ).thenThrow(
                new RuntimeException(
                        "Provider Down"
                )
        );

        // WHEN / THEN

        assertThrows(
                ExternalProviderException.class,
                () -> analysisService.analyze(
                        request
                )
        );

        verify(
                healthReportRepository,
                never()
        ).save(any());

        verify(
                alertRepository,
                never()
        ).saveAll(anyList());
    }

    @Test
    @DisplayName("Should return latest health report")
    void shouldReturnHealthReport() {

        // GIVEN

        String vin =
                "VF1RFA12345678901";

        HealthReport report =
                HealthReport.builder()
                        .vin(vin)
                        .healthScore(20)
                        .status(
                                VehicleHealthStatus.CRITICAL
                        )
                        .build();

        HealthReportResponse response =
                new HealthReportResponse(
                        vin,
                        20,
                        VehicleHealthStatus.CRITICAL,
                        ANALYZED_AT
                );

        when(
                healthReportRepository
                        .findFirstByVinOrderByAnalyzedAtDesc(
                                vin
                        )
        ).thenReturn(
                Optional.of(report)
        );

        when(
                analysisMapper.toHealthReportResponse(
                        report
                )
        ).thenReturn(
                response
        );

        // WHEN

        HealthReportResponse result =
                analysisService.getHealthReport(
                        vin
                );

        // THEN

        assertNotNull(result);

        assertEquals(
                vin,
                result.vin()
        );

        assertEquals(
                20,
                result.healthScore()
        );
    }

    @Test
    @DisplayName("Should throw HealthReportNotFoundException when no report exists")
    void shouldThrowHealthReportNotFoundException() {

        // GIVEN

        String vin =
                "UNKNOWN12345678901";

        when(
                healthReportRepository
                        .findFirstByVinOrderByAnalyzedAtDesc(
                                vin
                        )
        ).thenReturn(
                Optional.empty()
        );

        // WHEN / THEN

        assertThrows(
                HealthReportNotFoundException.class,
                () -> analysisService.getHealthReport(
                        vin
                )
        );
    }

    @Test
    @DisplayName("Should return alerts associated with a vehicle")
    void shouldReturnAlerts() {

        // GIVEN

        String vin =
                "VF1RFA12345678901";

        Alert alert =
                Alert.builder()
                        .vin(vin)
                        .type(
                                AlertType.LOW_BATTERY
                        )
                        .message(
                                "LOW_BATTERY"
                        )
                        .build();

        AlertResponse response =
                new AlertResponse(
                        AlertType.LOW_BATTERY,
                        "LOW_BATTERY"
                );

        when(
                alertRepository.findAllByVin(
                        vin
                )
        ).thenReturn(
                List.of(alert)
        );

        when(
                analysisMapper.toAlertResponse(
                        alert
                )
        ).thenReturn(
                response
        );

        // WHEN

        List<AlertResponse> result =
                analysisService.getAlerts(
                        vin
                );

        // THEN

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                AlertType.LOW_BATTERY,
                result.getFirst().type()
        );
    }

    private AnalysisRequest buildRequest() {

        return new AnalysisRequest(
                "VF1RFA12345678901",
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
    }
}