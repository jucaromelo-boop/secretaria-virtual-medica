package com.consultorio.pacientes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pacientesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Pacientes")
                        .description("Gestion de pacientes - Secretaria Virtual")
                        .version("1.0"));
    }
}