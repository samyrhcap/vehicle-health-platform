package com.renault.vehicle.analysis.dao.repository;

import com.renault.vehicle.analysis.dao.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findAllByVin(String vin);
}