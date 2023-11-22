package com.example.userservice.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class Properties {

	private final String secretKey;
	private final String tokenCreator;
	private final Long tokenAccessValidTime;
	private final String tokenAccessName;
	private final Long tokenRefreshValidTime;
	private final String tokenRefreshName;

	@ConstructorBinding
	public Properties(String secretKey, String tokenCreator, Long tokenAccessValidTime, String tokenAccessName, Long tokenRefreshValidTime, String tokenRefreshName) {
		this.secretKey = secretKey;
		this.tokenCreator = tokenCreator;
		this.tokenAccessValidTime = tokenAccessValidTime;
		this.tokenAccessName = tokenAccessName;
		this.tokenRefreshValidTime = tokenRefreshValidTime;
		this.tokenRefreshName = tokenRefreshName;
	}
}
