package com.renault.telemetry.dao.repository;

import com.renault.telemetry.dao.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {

    Optional<Telemetry> findFirstByVinOrderByReceivedAtDesc(String vin);
}
