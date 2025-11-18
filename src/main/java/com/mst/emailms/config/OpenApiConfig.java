package com.mst.emailms.config;

import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.List;

public class OpenApiConfig {

    @Value("${openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server server = new Server();
        server.setUrl(devUrl);
        server.setDescription("Alert-Hub UTRL for development ENV ");

        Contact contact = new Contact();
        contact.setEmail("omar99awaisha@gmail.com");
        contact.setName("omar");
        contact.setUrl("http://www.omarAwaish.com");
        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info=  new Info().title("Metrics microservice API").version("1.0").contact(contact)
                .description("This API exposes endpoints to manage books.").termsOfService("https://www.Kabhad82.com/terms")
                .license(mitLicense);
        return new OpenAPI().info(info).servers(List.of(server));
    }


}
