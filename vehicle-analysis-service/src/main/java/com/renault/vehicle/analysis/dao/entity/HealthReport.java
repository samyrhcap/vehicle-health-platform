package com.renault.vehicle.analysis.dao.entity;

import com.renault.vehicle.analysis.enums.VehicleHealthStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(
        name = "health_reports",
        indexes = {@Index(name = "idx_health_report_vin", columnList = "vin")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 17)
    private String vin;

    @Column(nullable = false)
    private Integer healthScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleHealthStatus status;

    @Column(nullable = false)
    private LocalDateTime analyzedAt;

    @PrePersist
    public void prePersist() {
        analyzedAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
}