package com.renault.telemetry;

import com.renault.telemetry.client.VehicleAnalysisClient;
import com.renault.telemetry.dao.entity.Telemetry;
import com.renault.telemetry.dao.repository.TelemetryRepository;
import com.renault.telemetry.dto.resquest.AnalysisRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Telemetry Service Integration Tests")
class TelemetryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TelemetryRepository telemetryRepository;
    @MockitoBean
    private VehicleAnalysisClient vehicleAnalysisClient;

    @Test
    @DisplayName(
            "Should persist telemetry and trigger analysis when a valid telemetry payload is submitted"
    )
    void shouldPersistTelemetrySuccessfully() throws Exception {

        doNothing()
                .when(vehicleAnalysisClient)
                .analyze(any(AnalysisRequest.class));

        long beforeCount = telemetryRepository.count();

        String vin = "VF1RFA12345678901";

        mockMvc.perform(
                        post("/api/v1/telemetry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":100000,
                                          "batteryLevel":80,
                                          "fuelLevel":50,
                                          "tirePressure":2.2,
                                          "engineTemperature":90,
                                          "errorCodes":["P001"]
                                        }
                                        """)
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.vin").value(vin))
                .andExpect(jsonPath("$.status").value("ACCEPTED"));

        assertThat(
                telemetryRepository.count()
        ).isEqualTo(
                beforeCount + 1
        );

        Telemetry telemetry =
                telemetryRepository
                        .findFirstByVinOrderByReceivedAtDesc(vin)
                        .orElseThrow();

        assertThat(
                telemetry.getVin()
        ).isEqualTo(vin);

        assertThat(
                telemetry.getMileage()
        ).isEqualTo(100000);

        assertThat(
                telemetry.getBatteryLevel()
        ).isEqualTo(80);

        assertThat(
                telemetry.getFuelLevel()
        ).isEqualTo(50);

        verify(vehicleAnalysisClient)
                .analyze(any(AnalysisRequest.class));
    }
}
