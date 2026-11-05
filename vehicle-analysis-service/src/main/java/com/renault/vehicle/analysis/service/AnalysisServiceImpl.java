package com.renault.vehicle.analysis.service;

import com.renault.vehicle.analysis.client.RecallProviderClient;
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
import com.renault.vehicle.analysis.service.helpers.AnalysisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AnalysisServiceImpl implements AnalysisService {

    private final HealthScoreService healthScoreService;
    private final AlertService alertService;
    private final RecallProviderClient recallProviderClient;
    private final AnalysisMapper analysisMapper;
    private final HealthReportRepository healthReportRepository;
    private final AlertRepository alertRepository;

    @Override
    public void analyze(
            AnalysisRequest request) {


        boolean activeRecall = retrieveRecallStatus(request.vin());

        int healthScore = healthScoreService.calculateHealthScore(request, activeRecall);

        VehicleHealthStatus status = AnalysisHelper.determineStatus(healthScore);

        HealthReport report = AnalysisHelper.buildHealthReport(request, healthScore, status);

        healthReportRepository.save(report);

        List<AlertType> alertTypes =
                alertService.generateAlerts(
                        request,
                        activeRecall
                );

        AnalysisHelper.persistAlerts(
                request.vin(),
                alertTypes,
                alertRepository
        );
    }

    private boolean retrieveRecallStatus(
            String vin) {

        try {

            RecallResponse recallResponse = recallProviderClient.getRecallInformation(vin);

            return recallResponse.activeRecall();

        } catch (Exception exception) {
            throw new ExternalProviderException(
                    "Recall provider unavailable"
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public HealthReportResponse getHealthReport(
            String vin) {

        HealthReport report = healthReportRepository.findFirstByVinOrderByAnalyzedAtDesc(vin)
                        .orElseThrow(
                                () -> new HealthReportNotFoundException(vin)
                        );

        return analysisMapper.toHealthReportResponse(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertResponse> getAlerts(
            String vin) {

        return alertRepository.findAllByVin(vin).stream().map(analysisMapper::toAlertResponse).toList();
    }
}