package com.renault.vehicle.analysis;

import com.renault.vehicle.analysis.service.AnalysisService;
import com.renault.vehicle.analysis.web.InternalAnalysisController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternalAnalysisController.class)
@DisplayName("Internal Analysis Controller Tests")
class InternalAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalysisService analysisService;

    @Test
    @DisplayName("Should return 202 Accepted for a valid analysis request")
    void shouldReturn202Accepted() throws Exception {

        // GIVEN

        doNothing()
                .when(analysisService)
                .analyze(any());

        // WHEN / THEN

        mockMvc.perform(
                        post("/internal/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":150000,
                                          "batteryLevel":15,
                                          "fuelLevel":5,
                                          "tirePressure":1.7,
                                          "engineTemperature":110,
                                          "errorCodes":["P001","P002"]
                                        }
                                        """)
                )
                .andExpect(
                        status().isAccepted()
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when VIN is empty")
    void shouldReturn400WhenVinIsEmpty() throws Exception {

        // WHEN / THEN

        mockMvc.perform(
                        post("/internal/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"",
                                          "mileage":150000,
                                          "batteryLevel":15,
                                          "fuelLevel":5,
                                          "tirePressure":1.7,
                                          "engineTemperature":110
                                        }
                                        """)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("ANL_003")
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when mileage is negative")
    void shouldReturn400WhenMileageIsNegative() throws Exception {

        // WHEN / THEN

        mockMvc.perform(
                        post("/internal/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":-1,
                                          "batteryLevel":15,
                                          "fuelLevel":5,
                                          "tirePressure":1.7,
                                          "engineTemperature":110
                                        }
                                        """)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("ANL_003")
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when battery level exceeds 100 percent")
    void shouldReturn400WhenBatteryLevelIsInvalid() throws Exception {

        // WHEN / THEN

        mockMvc.perform(
                        post("/internal/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":150000,
                                          "batteryLevel":101,
                                          "fuelLevel":5,
                                          "tirePressure":1.7,
                                          "engineTemperature":110
                                        }
                                        """)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("ANL_003")
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when fuel level exceeds 100 percent")
    void shouldReturn400WhenFuelLevelIsInvalid() throws Exception {

        // WHEN / THEN

        mockMvc.perform(
                        post("/internal/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":150000,
                                          "batteryLevel":15,
                                          "fuelLevel":101,
                                          "tirePressure":1.7,
                                          "engineTemperature":110
                                        }
                                        """)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("ANL_003")
                );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when tire pressure is not positive")
    void shouldReturn400WhenTirePressureIsInvalid() throws Exception {

        // WHEN / THEN

        mockMvc.perform(
                        post("/internal/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "vin":"VF1RFA12345678901",
                                          "mileage":150000,
                                          "batteryLevel":15,
                                          "fuelLevel":5,
                                          "tirePressure":0,
                                          "engineTemperature":110
                                        }
                                        """)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("ANL_003")
                );
    }
}