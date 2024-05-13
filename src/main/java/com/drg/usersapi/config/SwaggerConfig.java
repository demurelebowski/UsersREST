package com.drg.usersapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(apiInfo());
	}

	private Info apiInfo() {
		return new Info().title("User API")
				.description("API for users.")
				.license(new License().name("Apache License Version 2.0")
						.url("https://example.io/EXAMPLE"))
				.version("1.0");
	}

}
