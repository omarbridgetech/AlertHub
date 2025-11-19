package com.Alnsor.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI securityMsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Alert Hub - Security Service API")
                        .version("v1")
                        .description("JWT-based auth service providing users, roles, and permissions"));
    }
}
