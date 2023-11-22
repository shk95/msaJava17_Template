package com.example.userservice.auth;

import lombok.Getter;

@Getter
public enum JwtTokenType {
	ACCESS("ACCESS_TOKEN"), REFRESH("REFRESH_TOKEN");
	private final String value;

	JwtTokenType(String value) {
		this.value = value;
	}
}
