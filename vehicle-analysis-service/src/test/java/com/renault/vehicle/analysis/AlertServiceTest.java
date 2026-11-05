package com.renault.vehicle.analysis;

import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.enums.AlertType;
import com.renault.vehicle.analysis.service.AlertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Alert Service Tests")
class AlertServiceTest {

    private AlertServiceImpl alertService;

    @BeforeEach
    void setUp() {
        alertService = new AlertServiceImpl();
    }

    @Test
    @DisplayName("Should not generate alerts for a healthy vehicle")
    void shouldReturnNoAlertsForHealthyVehicle() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        90,
                        80,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        false
                );

        // THEN

        assertTrue(alerts.isEmpty());
    }

    @Test
    @DisplayName("Should generate LOW_BATTERY alert when battery level is below threshold")
    void shouldGenerateLowBatteryAlert() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        15,
                        80,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        false
                );

        // THEN

        assertTrue(
                alerts.contains(
                        AlertType.LOW_BATTERY
                )
        );
    }

    @Test
    @DisplayName("Should generate LOW_FUEL alert when fuel level is below threshold")
    void shouldGenerateLowFuelAlert() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        90,
                        5,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        false
                );

        // THEN

        assertTrue(
                alerts.contains(
                        AlertType.LOW_FUEL
                )
        );
    }

    @Test
    @DisplayName("Should generate LOW_TIRE_PRESSURE alert when tire pressure is below threshold")
    void shouldGenerateLowTirePressureAlert() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        90,
                        80,
                        1.7,
                        85.0,
                        List.of()
                );

        // WHEN

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        false
                );

        // THEN

        assertTrue(
                alerts.contains(
                        AlertType.LOW_TIRE_PRESSURE
                )
        );
    }

    @Test
    @DisplayName("Should generate ENGINE_OVERHEATING alert when engine temperature exceeds threshold")
    void shouldGenerateEngineOverheatingAlert() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        90,
                        80,
                        2.3,
                        110.0,
                        List.of()
                );

        // WHEN

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        false
                );

        // THEN

        assertTrue(
                alerts.contains(
                        AlertType.ENGINE_OVERHEATING
                )
        );
    }

    @Test
    @DisplayName("Should generate ERROR_CODES_DETECTED alert when diagnostic codes are present")
    void shouldGenerateErrorCodeAlert() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        90,
                        80,
                        2.3,
                        85.0,
                        List.of("P001")
                );

        // WHEN

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        false
                );

        // THEN

        assertTrue(
                alerts.contains(
                        AlertType.ERROR_CODES_DETECTED
                )
        );
    }

    @Test
    @DisplayName("Should generate VEHICLE_RECALL alert when an active recall exists")
    void shouldGenerateRecallAlert() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        90,
                        80,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        true
                );

        // THEN

        assertTrue(
                alerts.contains(
                        AlertType.VEHICLE_RECALL
                )
        );
    }

    @Test
    @DisplayName("Should not generate ERROR_CODES_DETECTED alert when error codes are null")
    void shouldNotGenerateErrorCodeAlertWhenErrorCodesAreNull() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        90,
                        80,
                        2.3,
                        85.0,
                        null
                );

        // WHEN

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        false
                );

        // THEN

        assertFalse(
                alerts.contains(
                        AlertType.ERROR_CODES_DETECTED
                )
        );
    }

    @Test
    @DisplayName("Should not generate ERROR_CODES_DETECTED alert when error codes list is empty")
    void shouldNotGenerateErrorCodeAlertWhenErrorCodesAreEmpty() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        90,
                        80,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        false
                );

        // THEN

        assertFalse(
                alerts.contains(
                        AlertType.ERROR_CODES_DETECTED
                )
        );
    }

    @Test
    @DisplayName("Should generate all alerts for a critical vehicle")
    void shouldGenerateAllAlertsForCriticalVehicle() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
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

        List<AlertType> alerts =
                alertService.generateAlerts(
                        request,
                        true
                );

        // THEN

        assertEquals(6, alerts.size());

        assertTrue(alerts.contains(AlertType.LOW_BATTERY));
        assertTrue(alerts.contains(AlertType.LOW_FUEL));
        assertTrue(alerts.contains(AlertType.LOW_TIRE_PRESSURE));
        assertTrue(alerts.contains(AlertType.ENGINE_OVERHEATING));
        assertTrue(alerts.contains(AlertType.ERROR_CODES_DETECTED));
        assertTrue(alerts.contains(AlertType.VEHICLE_RECALL));
    }

    private AnalysisRequest buildRequest(
            Integer battery,
            Integer fuel,
            Double tirePressure,
            Double temperature,
            List<String> errors) {

        return new AnalysisRequest(
                "VF1RFA12345678901",
                150000,
                battery,
                fuel,
                tirePressure,
                temperature,
                errors
        );
    }
}