package com.renault.vehicle.analysis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vehicleAnalysisApi() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Vehicle Analysis Service API")
                                .version("1.0")
                                .description("""
                                        Vehicle Analysis Service is responsible for:
                                        
                                        • Processing vehicle telemetry data
                                        • Calculating health scores
                                        • Detecting anomalies
                                        • Generating vehicle alerts
                                        • Exposing health reports
                                        """)
                                .contact(
                                        new Contact()
                                                .name("Renault Vehicle Health Platform")
                                )
                                .license(
                                        new License()
                                                .name("Internal Use")
                                )
                );
    }
}