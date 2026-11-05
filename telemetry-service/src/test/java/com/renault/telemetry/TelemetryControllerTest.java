package com.renault.telemetry;

import com.renault.telemetry.dto.response.TelemetryResponse;
import com.renault.telemetry.service.TelemetryService;
import com.renault.telemetry.web.TelemetryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Telemetry Controller API Tests")
@WebMvcTest(TelemetryController.class)
class TelemetryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TelemetryService telemetryService;

    @Test
    @DisplayName("Should return 202 Accepted when telemetry payload is valid")
    void shouldReturn202Accepted() throws Exception {

        TelemetryResponse response =
                new TelemetryResponse(
                        "VF1RFA12345678901",
                        "ACCEPTED",
                        LocalDateTime.now()
                );

        when(telemetryService.receiveTelemetry(any())).thenReturn(response);

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
                .andExpect(
                        jsonPath("$.vin")
                        .value("VF1RFA12345678901")
                )
                .andExpect(
                        jsonPath("$.status")
                        .value("ACCEPTED")
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when VIN is empty")
    void shouldReturn400WhenVinIsEmpty() throws Exception {

        mockMvc.perform(
                        post("/api/v1/telemetry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"",
                                          "mileage":100000,
                                          "batteryLevel":80,
                                          "fuelLevel":50,
                                          "tirePressure":2.2,
                                          "engineTemperature":90
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.code")
                        .value("TEL_004")
                )
                .andExpect(
                        jsonPath("$.code")
                        .value("TEL_004")
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when VIN length is invalid")
    void shouldReturn400WhenVinLengthIsInvalid() throws Exception {

        mockMvc.perform(
                        post("/api/v1/telemetry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"123",
                                          "mileage":100000,
                                          "batteryLevel":80,
                                          "fuelLevel":50,
                                          "tirePressure":2.2,
                                          "engineTemperature":90
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.code")
                        .value("TEL_004")
                )
                .andExpect(
                        jsonPath("$.message")
                        .value("VIN must contain exactly 17 characters")
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when mileage is negative")
    void shouldReturn400WhenMileageIsNegative() throws Exception {

        mockMvc.perform(
                        post("/api/v1/telemetry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":-1,
                                          "batteryLevel":80,
                                          "fuelLevel":50,
                                          "tirePressure":2.2,
                                          "engineTemperature":90
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message")
                        .value("Mileage cannot be negative")
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when battery level exceeds 100")
    void shouldReturn400WhenBatteryLevelIsAbove100() throws Exception {

        mockMvc.perform(
                        post("/api/v1/telemetry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":100000,
                                          "batteryLevel":101,
                                          "fuelLevel":50,
                                          "tirePressure":2.2,
                                          "engineTemperature":90
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message")
                                .value("Battery level must be between 0 and 100")
                );
    }

    @Test
    void shouldReturn400WhenFuelLevelIsAbove100() throws Exception {

        mockMvc.perform(
                        post("/api/v1/telemetry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":100000,
                                          "batteryLevel":80,
                                          "fuelLevel":101,
                                          "tirePressure":2.2,
                                          "engineTemperature":90
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message")
                        .value("Fuel level must be between 0 and 100")
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when tire pressure is negative")
    void shouldReturn400WhenTirePressureIsNegative() throws Exception {

        mockMvc.perform(
                        post("/api/v1/telemetry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":100000,
                                          "batteryLevel":80,
                                          "fuelLevel":50,
                                          "tirePressure":-1,
                                          "engineTemperature":90
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message")
                        .value("Tire pressure must be greater than 0")
                );
    }
}