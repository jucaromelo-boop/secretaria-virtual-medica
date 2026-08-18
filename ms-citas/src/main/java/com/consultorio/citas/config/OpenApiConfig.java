package com.consultorio.citas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI citasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Citas")
                        .description("Gestion de citas medicas - Secretaria Virtual")
                        .version("1.0"));
    }
}