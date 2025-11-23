package com.alerthub.evaluation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Evaluation Service API",
				version = "1.0",
				description = "Developer evaluation and statistics microservice for Alert Hub system",
				contact = @Contact(
						name = "Alert Hub Team",
						email = "support@alerthub.com"
				)
		)
)
public class EvaluationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvaluationApplication.class, args);
	}

}
