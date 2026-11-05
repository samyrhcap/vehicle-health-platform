package com.renault.telemetry.mapper;

import com.renault.telemetry.dao.entity.Telemetry;
import com.renault.telemetry.dto.resquest.AnalysisRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalysisRequestMapper {

    AnalysisRequest toAnalysisRequest(Telemetry telemetry);
}