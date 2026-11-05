package com.renault.vehicle.analysis.mapper;

import com.renault.vehicle.analysis.dao.entity.Alert;
import com.renault.vehicle.analysis.dao.entity.HealthReport;
import com.renault.vehicle.analysis.dto.response.AlertResponse;
import com.renault.vehicle.analysis.dto.response.HealthReportResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalysisMapper {

    HealthReportResponse toHealthReportResponse(HealthReport healthReport);

    AlertResponse toAlertResponse(Alert alert);
}
