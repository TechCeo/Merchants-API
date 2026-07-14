package com.portfolio.merchantapi.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Merchant API",
                version = "v1",
                description = "REST API for merchant and transaction management with Flyway migrations, structured errors, and transaction idempotency."
        )
)
public class OpenApiConfig {
}
