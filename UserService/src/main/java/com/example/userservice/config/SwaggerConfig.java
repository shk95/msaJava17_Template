package com.example.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	private Info info() {
		return new Info()
				.title("User Service API")
				.description("foo bar baz")
				.contact(new Contact()
						.name("foo")
						.email("bar")
						.url("baz"))
				.license(new License()
						.name("asdf"))
				.version("1.0.0");
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(info());
	}
}
