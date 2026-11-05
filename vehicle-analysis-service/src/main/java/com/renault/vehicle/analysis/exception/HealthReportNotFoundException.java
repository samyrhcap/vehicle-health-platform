package com.renault.vehicle.analysis.exception;

import com.renault.vehicle.analysis.enums.ErrorCode;

public class HealthReportNotFoundException extends BusinessException {

    public HealthReportNotFoundException(String vin) {

        super(
                ErrorCode.HEALTH_REPORT_NOT_FOUND,
                "No health report found for VIN: " + vin
        );
    }
}