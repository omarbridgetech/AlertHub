package com.mst.metricms.config;

import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration for Metrics microservice.
 * Provides interactive API documentation.
 */
@Configuration
public class SwaggerConfig {

    @Value("${openapi.dev-url}")
    private String devUrl;


    @Bean
    public OpenAPI myOpenAPI(){
        Server server = new Server();
        server.setUrl(devUrl);
        server.setDescription("OpenAPI Documentation for Metrics microservice");
        Contact contact = new Contact();
        contact.setEmail("omar99awaisha@gmail.com");
        contact.setName("omar");
        contact.setUrl("http://www.omar.com");
        //Li mitLicense= new License().name("MIT License").url("https://chooselicense.com/licenses/mit/");
        Info info = new Info().title("Metrics Microservice").version("1.0").contact(contact).
                description("Metrics Microservice").termsOfService("http://www.omar.com/terms");

        return new OpenAPI().info(info).servers(List.of(server));

    }



}
