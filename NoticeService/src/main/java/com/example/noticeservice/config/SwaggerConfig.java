package com.example.noticeservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	private Info apiInfo() {
		return new Info()
				.title("NoticeService")
				.description("Notice Service Description")
				.contact(new Contact().name("foo")
						.email("email")
						.url("xxx"))
				.license(new License()
						.name("bar"))
				.version("1.0.0");
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().components(new Components()).info(apiInfo());
	}
}
