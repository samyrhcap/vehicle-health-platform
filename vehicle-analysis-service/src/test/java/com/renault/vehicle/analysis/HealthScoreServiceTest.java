package com.renault.vehicle.analysis;

import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.service.HealthScoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Health Score Service Tests")
class HealthScoreServiceTest {

    private HealthScoreServiceImpl healthScoreService;

    @BeforeEach
    void setUp() {
        healthScoreService = new HealthScoreServiceImpl();
    }

    @Test
    @DisplayName("Should return perfect score for a healthy vehicle")
    void shouldReturnPerfectScore() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        50000,
                        90,
                        80,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        false
                );

        // THEN

        assertEquals(
                100,
                score
        );
    }

    @Test
    @DisplayName("Should apply mileage penalty for vehicles above 100000 km")
    void shouldApplyMileagePenalty() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        150000,
                        90,
                        80,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        false
                );

        // THEN

        assertEquals(
                90,
                score
        );
    }

    @Test
    @DisplayName("Should apply maximum mileage penalty for vehicles above 200000 km")
    void shouldApplyHighMileagePenalty() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        250000,
                        90,
                        80,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        false
                );

        // THEN

        assertEquals(
                80,
                score
        );
    }

    @Test
    @DisplayName("Should apply low battery penalty")
    void shouldApplyBatteryPenalty() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        50000,
                        15,
                        80,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        false
                );

        // THEN

        assertEquals(
                80,
                score
        );
    }

    @Test
    @DisplayName("Should apply low fuel penalty")
    void shouldApplyFuelPenalty() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        50000,
                        90,
                        5,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        false
                );

        // THEN

        assertEquals(
                95,
                score
        );
    }

    @Test
    @DisplayName("Should apply low tire pressure penalty")
    void shouldApplyTirePressurePenalty() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        50000,
                        90,
                        80,
                        1.7,
                        85.0,
                        List.of()
                );

        // WHEN

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        false
                );

        // THEN

        assertEquals(
                90,
                score
        );
    }

    @Test
    @DisplayName("Should apply engine overheating penalty")
    void shouldApplyEngineTemperaturePenalty() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        50000,
                        90,
                        80,
                        2.3,
                        110.0,
                        List.of()
                );

        // WHEN

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        false
                );

        // THEN

        assertEquals(
                85,
                score
        );
    }

    @Test
    @DisplayName("Should apply diagnostic error codes penalty")
    void shouldApplyErrorCodesPenalty() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        50000,
                        90,
                        80,
                        2.3,
                        85.0,
                        List.of(
                                "P001",
                                "P002"
                        )
                );

        // WHEN

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        false
                );

        // THEN

        assertEquals(
                90,
                score
        );
    }

    @Test
    @DisplayName("Should apply active recall penalty")
    void shouldApplyRecallPenalty() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
                        50000,
                        90,
                        80,
                        2.3,
                        85.0,
                        List.of()
                );

        // WHEN

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        true
                );

        // THEN

        assertEquals(
                90,
                score
        );
    }

    @Test
    @DisplayName("Should calculate critical vehicle score correctly")
    void shouldCalculateCriticalVehicleScore() {

        // GIVEN

        AnalysisRequest request =
                buildRequest(
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

        int score =
                healthScoreService.calculateHealthScore(
                        request,
                        true
                );

        // THEN

        assertEquals(
                20,
                score
        );
    }

    private AnalysisRequest buildRequest(
            Integer mileage,
            Integer battery,
            Integer fuel,
            Double tirePressure,
            Double temperature,
            List<String> errors) {

        return new AnalysisRequest(
                "VF1RFA12345678901",
                mileage,
                battery,
                fuel,
                tirePressure,
                temperature,
                errors
        );
    }
}