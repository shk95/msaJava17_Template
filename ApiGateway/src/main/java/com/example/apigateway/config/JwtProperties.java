package com.example.apigateway.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private final String secretKey;
	private final String tokenCreator;
	private final Long tokenAccessValidTime;
	private final String tokenAccessName;
	private final String tokenRefreshName;
	public static final String HEADER_PREFIX = "Bearer ";

	@ConstructorBinding
	public JwtProperties(String secretKey, String tokenCreator, Long tokenAccessValidTime, String tokenAccessName, String tokenRefreshName) {
		this.secretKey = secretKey;
		this.tokenCreator = tokenCreator;
		this.tokenAccessValidTime = tokenAccessValidTime;
		this.tokenAccessName = tokenAccessName;
		this.tokenRefreshName = tokenRefreshName;
	}
}
