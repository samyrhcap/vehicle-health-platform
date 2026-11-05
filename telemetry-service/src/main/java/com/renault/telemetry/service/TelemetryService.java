package com.renault.telemetry.service;


import com.renault.telemetry.dto.response.TelemetryResponse;
import com.renault.telemetry.dto.resquest.TelemetryRequest;

public interface TelemetryService {

    TelemetryResponse receiveTelemetry(TelemetryRequest request);
}