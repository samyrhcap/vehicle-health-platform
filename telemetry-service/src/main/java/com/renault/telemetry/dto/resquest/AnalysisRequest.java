package com.renault.telemetry.dto.resquest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

@Schema(
        name = "AnalysisRequest",
        description = "Internal payload sent to the vehicle analysis service"
)
public record AnalysisRequest(

        @Schema(
                description = "Vehicle Identification Number",
                example = "VF1RFA12345678901"
        )
        @NotBlank(message = "VIN is mandatory")
        @Size(
                min = 17,
                max = 17,
                message = "VIN must contain exactly 17 characters"
        )
        String vin,

        @Schema(
                description = "Vehicle mileage in kilometers",
                example = "150000"
        )
        @NotNull(message = "Mileage is mandatory")
        @PositiveOrZero(message = "Mileage cannot be negative")
        Integer mileage,

        @Schema(
                description = "Battery level percentage",
                example = "15"
        )
        @NotNull(message = "Battery level is mandatory")
        @Min(value = 0, message = "Battery level must be between 0 and 100")
        @Max(value = 100, message = "Battery level must be between 0 and 100")
        Integer batteryLevel,

        @Schema(
                description = "Fuel level percentage",
                example = "5"
        )
        @NotNull(message = "Fuel level is mandatory")
        @Min(value = 0, message = "Fuel level must be between 0 and 100")
        @Max(value = 100, message = "Fuel level must be between 0 and 100")
        Integer fuelLevel,

        @Schema(
                description = "Tire pressure in bar",
                example = "1.7"
        )
        @NotNull(message = "Tire pressure is mandatory")
        @Positive(message = "Tire pressure must be greater than zero")
        Double tirePressure,

        @Schema(
                description = "Engine temperature in Celsius",
                example = "110"
        )
        @NotNull(message = "Engine temperature is mandatory")
        Double engineTemperature,

        @Schema(
                description = "List of vehicle diagnostic error codes",
                example = "[\"P001\",\"P002\"]"
        )
        List<String> errorCodes

) {
}