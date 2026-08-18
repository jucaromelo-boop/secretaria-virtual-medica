package com.consultorio.medicos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI medicosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Medicos")
                        .description("Gestion de medicos, especialidades y consultorios - Secretaria Virtual")
                        .version("1.0"));
    }
}