package com.Alnsor.User.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MST Alert Hub - User Microservice API")
                        .description("User management microservice for MST Alert Hub system. Manages users, roles, and permissions.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MST Alert Hub Team")
                                .email("support@mstalerthub.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
