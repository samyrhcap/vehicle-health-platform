package com.renault.telemetry.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "telemetry",
        indexes = { @Index(name = "idx_telemetry_vin", columnList = "vin")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 17)
    private String vin;

    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false)
    private Integer batteryLevel;

    @Column(nullable = false)
    private Integer fuelLevel;

    @Column(nullable = false)
    private Double tirePressure;

    @Column(nullable = false)
    private Double engineTemperature;

    @Column(nullable = false, updatable = false)
    private LocalDateTime receivedAt;

    @PrePersist
    public void prePersist() {
        this.receivedAt = LocalDateTime.now();
    }

    @ElementCollection
    @CollectionTable(name = "telemetry_error_codes", joinColumns = @JoinColumn(name = "telemetry_id"))
    @Column(name = "error_code")
    private List<String> errorCodes;

}