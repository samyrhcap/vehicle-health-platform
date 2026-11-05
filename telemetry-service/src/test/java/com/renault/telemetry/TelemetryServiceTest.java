package com.renault.telemetry;

import com.renault.telemetry.client.VehicleAnalysisClient;
import com.renault.telemetry.dao.entity.Telemetry;
import com.renault.telemetry.dao.repository.TelemetryRepository;
import com.renault.telemetry.dto.response.TelemetryResponse;
import com.renault.telemetry.dto.resquest.AnalysisRequest;
import com.renault.telemetry.dto.resquest.TelemetryRequest;
import com.renault.telemetry.exception.ExternalAnalysisServiceException;
import com.renault.telemetry.mapper.AnalysisRequestMapper;
import com.renault.telemetry.mapper.TelemetryMapper;
import com.renault.telemetry.service.TelemetryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Telemetry Service Unit Tests")
class TelemetryServiceTest {

    @Mock
    private TelemetryRepository telemetryRepository;
    @Mock
    private TelemetryMapper telemetryMapper;
    @Mock
    private VehicleAnalysisClient vehicleAnalysisClient;
    @Mock
    private AnalysisRequestMapper analysisRequestMapper;
    @InjectMocks
    private TelemetryServiceImpl telemetryService;


    @Test
    @DisplayName("Should persist telemetry and trigger vehicle analysis successfully")
    void shouldReceiveTelemetrySuccessfully() {

        TelemetryRequest request =
                new TelemetryRequest(
                        "VF1RFA12345678901",
                        100000,
                        80,
                        50,
                        2.2,
                        90.0,
                        List.of("P001")
                );

        Telemetry telemetry =
                Telemetry.builder()
                        .vin(request.vin())
                        .mileage(request.mileage())
                        .batteryLevel(request.batteryLevel())
                        .fuelLevel(request.fuelLevel())
                        .tirePressure(request.tirePressure())
                        .engineTemperature(request.engineTemperature())
                        .errorCodes(request.errorCodes())
                        .build();

        AnalysisRequest analysisRequest =
                new AnalysisRequest(
                        request.vin(),
                        request.mileage(),
                        request.batteryLevel(),
                        request.fuelLevel(),
                        request.tirePressure(),
                        request.engineTemperature(),
                        request.errorCodes()
                );

        TelemetryResponse expectedResponse =
                new TelemetryResponse(
                        request.vin(),
                        "ACCEPTED",
                        LocalDateTime.now()
                );

        when(
                telemetryMapper.toEntity(request)
        ).thenReturn(telemetry);

        when(
                telemetryRepository.save(telemetry)
        ).thenReturn(telemetry);

        when(
                analysisRequestMapper.toAnalysisRequest(telemetry)
        ).thenReturn(analysisRequest);

        when(
                telemetryMapper.toResponse(telemetry)
        ).thenReturn(expectedResponse);


        TelemetryResponse response =
                telemetryService.receiveTelemetry(request);


        verify(telemetryRepository).save(telemetry);

        verify(vehicleAnalysisClient).analyze(analysisRequest);

        verify(telemetryMapper).toResponse(telemetry);

        assertEquals(
                expectedResponse.vin(),
                response.vin()
        );

        assertEquals(
                "ACCEPTED",
                response.status()
        );
    }

    @Test
    @DisplayName("Should throw ExternalAnalysisServiceException when analysis service is unavailable")
    void shouldThrowExternalAnalysisServiceException() {

        TelemetryRequest request =
                new TelemetryRequest(
                        "VF1RFA12345678901",
                        100000,
                        80,
                        50,
                        2.2,
                        90.0,
                        List.of("P001")
                );

        Telemetry telemetry =
                Telemetry.builder()
                        .vin(request.vin())
                        .build();

        AnalysisRequest analysisRequest =
                new AnalysisRequest(
                        request.vin(),
                        request.mileage(),
                        request.batteryLevel(),
                        request.fuelLevel(),
                        request.tirePressure(),
                        request.engineTemperature(),
                        request.errorCodes()
                );

        when(
                telemetryMapper.toEntity(request)
        ).thenReturn(telemetry);

        when(
                telemetryRepository.save(telemetry)
        ).thenReturn(telemetry);

        when(
                analysisRequestMapper.toAnalysisRequest(telemetry)
        ).thenReturn(analysisRequest);

        doThrow(
                new RuntimeException("Feign Error")
        ).when(
                vehicleAnalysisClient
        ).analyze(analysisRequest);

        assertThrows(
                ExternalAnalysisServiceException.class,
                () -> telemetryService.receiveTelemetry(request)
        );

        verify(vehicleAnalysisClient).analyze(analysisRequest);
    }
}