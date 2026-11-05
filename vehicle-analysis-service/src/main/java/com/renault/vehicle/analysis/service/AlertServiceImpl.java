package com.renault.vehicle.analysis.service;

import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.enums.AlertType;
import com.renault.vehicle.analysis.service.helpers.AlertRuleHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    @Override
    public List<AlertType> generateAlerts(AnalysisRequest request, boolean activeRecall) {

        List<AlertType> alerts = new ArrayList<>();

        if (AlertRuleHelper.isLowBattery(request)) {
            alerts.add(AlertType.LOW_BATTERY);
        }

        if (AlertRuleHelper.isLowFuel(request)) {
            alerts.add(AlertType.LOW_FUEL);
        }

        if (AlertRuleHelper.isLowTirePressure(request)) {
            alerts.add(AlertType.LOW_TIRE_PRESSURE);
        }

        if (AlertRuleHelper.isEngineOverheating(request)) {
            alerts.add(AlertType.ENGINE_OVERHEATING);
        }

        if (AlertRuleHelper.hasErrorCodes(request)) {
            alerts.add(AlertType.ERROR_CODES_DETECTED);
        }

        if (AlertRuleHelper.hasActiveRecall(activeRecall)) {
            alerts.add(AlertType.VEHICLE_RECALL);
        }

        return alerts;
    }
}