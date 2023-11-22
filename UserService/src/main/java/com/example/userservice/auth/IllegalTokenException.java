package com.example.userservice.auth;

public class IllegalTokenException extends RuntimeException {

	public IllegalTokenException(String message) {
		super(message);
	}
}
