package com.renault.telemetry.service;

import com.renault.telemetry.client.VehicleAnalysisClient;
import com.renault.telemetry.dao.entity.Telemetry;
import com.renault.telemetry.dao.repository.TelemetryRepository;
import com.renault.telemetry.dto.response.TelemetryResponse;
import com.renault.telemetry.dto.resquest.TelemetryRequest;
import com.renault.telemetry.exception.ExternalAnalysisServiceException;
import com.renault.telemetry.mapper.AnalysisRequestMapper;
import com.renault.telemetry.mapper.TelemetryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TelemetryServiceImpl implements TelemetryService {

    private final TelemetryRepository telemetryRepository;
    private final TelemetryMapper telemetryMapper;
    private final VehicleAnalysisClient vehicleAnalysisClient;
    private final AnalysisRequestMapper analysisRequestMapper;

    @Override
    public TelemetryResponse receiveTelemetry(
            TelemetryRequest request) {


        Telemetry telemetry = telemetryMapper.toEntity(request);

        Telemetry savedTelemetry = telemetryRepository.save(telemetry);

        var analysisRequest = analysisRequestMapper.toAnalysisRequest(savedTelemetry);

        try {

            vehicleAnalysisClient.analyze(analysisRequest);

        } catch (Exception ex) {
            throw new ExternalAnalysisServiceException();
        }

        return telemetryMapper.toResponse(savedTelemetry);
    }
}