package com.example.apigateway.jwt;

import com.example.apigateway.config.JwtProperties;
import com.example.apigateway.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	private final JwtProperties properties;

	public String createToken(String userid, String roles) {
		Claims claims = Jwts.claims()
				.setIssuer(properties.getTokenCreator())
				.setSubject(userid);
		claims.put("roles", roles);
		Date now = new Date();
		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.getSecretKey()));
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + properties.getTokenAccessValidTime() * 1000L))
				.signWith(secretKey, SignatureAlgorithm.HS256)
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

	public String resolveToken(ServerHttpRequest request, JwtTokenType tokenType) {
		String tokenName = switch (tokenType) {
			case ACCESS_TOKEN -> properties.getTokenAccessName();
			case REFRESH_TOKEN -> properties.getTokenRefreshName();
		};
		HttpCookie cookie = request.getCookies().getFirst(tokenName);
		if (cookie == null) {
			return "";
		}
		String token = cookie.getValue();
		// Cookies에 토큰이 존재하지 않으면, Baerer 토큰에 값이 있는지 확인함
		if (token.isEmpty()) {
			String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
			log.info("bearerToken : " + bearerToken);
			if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProperties.HEADER_PREFIX)) {
				token = bearerToken.substring(7);
			}
			log.info("bearerToken token : " + token);
		}
		return token;
	}

	public Authentication getAuthentication(String token) {
		TokenDto tokenDto = getTokenInfo(token);
		String userid = tokenDto.userid();
		String roles = tokenDto.role();
		Set<GrantedAuthority> authorities = Arrays.stream(roles.split(","))
				.flatMap(role -> Stream.of(new SimpleGrantedAuthority(role)))
				.collect(Collectors.toSet());
		return new UsernamePasswordAuthenticationToken(userid, "", authorities);
	}

	public JwtStatus validateToken(String token) {
		if (token.isEmpty()) {
			return JwtStatus.DENIED;
		}
		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.getSecretKey()));
		try {
			Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
			return JwtStatus.EXPIRED;
		} catch (JwtException e) {
			return JwtStatus.DENIED;
		}
		return JwtStatus.ACCESS;
	}
}
