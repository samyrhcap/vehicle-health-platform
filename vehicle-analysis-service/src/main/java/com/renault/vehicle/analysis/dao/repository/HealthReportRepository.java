package com.renault.vehicle.analysis.dao.repository;

import com.renault.vehicle.analysis.dao.entity.HealthReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {

    Optional<HealthReport> findFirstByVinOrderByAnalyzedAtDesc(String vin);
}