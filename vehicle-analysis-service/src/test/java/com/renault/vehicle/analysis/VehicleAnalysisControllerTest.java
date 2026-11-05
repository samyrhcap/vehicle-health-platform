package com.renault.vehicle.analysis;

import com.renault.vehicle.analysis.dto.response.AlertResponse;
import com.renault.vehicle.analysis.dto.response.HealthReportResponse;
import com.renault.vehicle.analysis.enums.AlertType;
import com.renault.vehicle.analysis.enums.VehicleHealthStatus;
import com.renault.vehicle.analysis.exception.HealthReportNotFoundException;
import com.renault.vehicle.analysis.service.AnalysisService;
import com.renault.vehicle.analysis.web.VehicleAnalysisController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleAnalysisController.class)
@DisplayName("Vehicle Analysis Controller Tests")
class VehicleAnalysisControllerTest {

    private static final LocalDateTime ANALYZED_AT =
            LocalDateTime.of(
                    2026,
                    1,
                    1,
                    0,
                    0
            );

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalysisService analysisService;

    @Test
    @DisplayName("Should return health report for an existing vehicle")
    void shouldReturnHealthReport() throws Exception {

        // GIVEN

        String vin =
                "VF1RFA12345678901";

        HealthReportResponse response =
                new HealthReportResponse(
                        vin,
                        20,
                        VehicleHealthStatus.CRITICAL,
                        ANALYZED_AT
                );

        when(
                analysisService.getHealthReport(vin)
        ).thenReturn(
                response
        );

        // WHEN / THEN

        mockMvc.perform(
                        get(
                                "/api/v1/vehicles/{vin}/health-report",
                                vin
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.vin")
                                .value(vin)
                )
                .andExpect(
                        jsonPath("$.healthScore")
                                .value(20)
                )
                .andExpect(
                        jsonPath("$.status")
                                .value("CRITICAL")
                );
    }

    @Test
    @DisplayName("Should return 404 when health report does not exist")
    void shouldReturn404WhenHealthReportNotFound()
            throws Exception {

        // GIVEN

        String vin =
                "UNKNOWN12345678901";

        when(
                analysisService.getHealthReport(vin)
        ).thenThrow(
                new HealthReportNotFoundException(
                        vin
                )
        );

        // WHEN / THEN

        mockMvc.perform(
                        get(
                                "/api/v1/vehicles/{vin}/health-report",
                                vin
                        )
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("ANL_001")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Health report not found")
                );
    }

    @Test
    @DisplayName("Should return all alerts associated with a vehicle")
    void shouldReturnAlerts()
            throws Exception {

        // GIVEN

        String vin =
                "VF1RFA12345678901";

        List<AlertResponse> alerts =
                List.of(
                        new AlertResponse(
                                AlertType.LOW_BATTERY,
                                "LOW_BATTERY"
                        ),
                        new AlertResponse(
                                AlertType.VEHICLE_RECALL,
                                "VEHICLE_RECALL"
                        )
                );

        when(
                analysisService.getAlerts(vin)
        ).thenReturn(
                alerts
        );

        // WHEN / THEN

        mockMvc.perform(
                        get(
                                "/api/v1/vehicles/{vin}/alerts",
                                vin
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$[0].type")
                                .value("LOW_BATTERY")
                )
                .andExpect(
                        jsonPath("$[0].message")
                                .value("LOW_BATTERY")
                )
                .andExpect(
                        jsonPath("$[1].type")
                                .value("VEHICLE_RECALL")
                );
    }

    @Test
    @DisplayName("Should return an empty alerts list when no alerts exist")
    void shouldReturnEmptyAlertsList()
            throws Exception {

        // GIVEN

        String vin =
                "UNKNOWN12345678901";

        when(
                analysisService.getAlerts(vin)
        ).thenReturn(
                List.of()
        );

        // WHEN / THEN

        mockMvc.perform(
                        get(
                                "/api/v1/vehicles/{vin}/alerts",
                                vin
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$")
                                .isArray()
                );
    }
}