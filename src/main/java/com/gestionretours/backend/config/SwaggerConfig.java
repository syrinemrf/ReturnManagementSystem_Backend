package com.gestionretours.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration.
 * Configuration OpenAPI / Swagger.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gestion des Retours API",
                version = "1.0.0",
                description = "API REST for product return management system / " +
                        "API REST pour le système de gestion des retours produits"
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
