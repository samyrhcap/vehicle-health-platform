package com.renault.telemetry.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI telemetryApi() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Telemetry Service API")
                                .version("1.0")
                                .description("""
                                        Telemetry Service receives vehicle telemetry,
                                        stores it and triggers vehicle health analysis.
                                        """)
                                .contact(
                                        new Contact()
                                                .name("Renault Vehicle Health Platform")
                                )
                );
    }
}