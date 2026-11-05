package com.renault.vehicle.analysis.service;

import com.renault.vehicle.analysis.dto.request.AnalysisRequest;
import com.renault.vehicle.analysis.enums.AlertType;
import java.util.List;

public interface AlertService {

    List<AlertType> generateAlerts(AnalysisRequest request, boolean activeRecall);
}