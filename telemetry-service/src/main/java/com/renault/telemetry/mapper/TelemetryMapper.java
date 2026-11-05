package com.renault.telemetry.mapper;

import com.renault.telemetry.dao.entity.Telemetry;
import com.renault.telemetry.dto.response.TelemetryResponse;
import com.renault.telemetry.dto.resquest.TelemetryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TelemetryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receivedAt", ignore = true)
    Telemetry toEntity(TelemetryRequest request);

    @Mapping(target = "status", constant = "ACCEPTED")
    TelemetryResponse toResponse(Telemetry telemetry);
}