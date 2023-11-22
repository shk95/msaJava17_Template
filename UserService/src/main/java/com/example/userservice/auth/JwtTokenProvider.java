package com.example.userservice.auth;

import com.example.userservice.config.Properties;
import com.example.userservice.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RefreshScope
@Component
public class JwtTokenProvider {

	private final Properties properties;
	public static final String HEADER_PREFIX = "Bearer ";

	public String createToken(String userid, String roles, JwtTokenType tokenType) {
		long validTime = switch (tokenType) {
			case ACCESS -> properties.getTokenAccessValidTime();
			case REFRESH -> properties.getTokenRefreshValidTime();
		};
		Claims claims = Jwts.claims()
				.setIssuer(properties.getTokenCreator())
				.setSubject(userid);
		claims.put("roles", roles);

		SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.getSecretKey()));

		Instant now = Instant.now();
		return Jwts.builder()
				.setClaims(claims)
				.signWith(secret, SignatureAlgorithm.HS256)
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plus(validTime, ChronoUnit.MILLIS)))
				.compact();
	}

	public TokenDto getTokenInfo(String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.getSecretKey()));

		Claims claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		String userId = claims.getSubject();
		String role = (String) claims.get("roles");

		return TokenDto.builder()
				.userid(claims.getSubject())
				.role((String) claims.get("roles"))
				.build();
	}

	public String resolveToken(HttpServletRequest request, JwtTokenType tokenType) {
		String tokenName = switch (tokenType) {
			case ACCESS -> properties.getTokenAccessName();
			case REFRESH -> properties.getTokenRefreshName();
		};
		return Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals(tokenName))
				.findFirst()
				.map(Cookie::getValue)
				.orElseGet(
						() -> Optional.ofNullable(request.getHeader("Authorization"))
								.filter(header -> header.startsWith(HEADER_PREFIX))
								.map(header -> header.substring(HEADER_PREFIX.length()))
								.orElse("")
				);
	}
}
