package com.example.apigateway.filter;

import com.example.apigateway.config.JwtProperties;
import com.example.apigateway.dto.TokenDto;
import com.example.apigateway.jwt.JwtStatus;
import com.example.apigateway.jwt.JwtTokenProvider;
import com.example.apigateway.jwt.JwtTokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter implements WebFilter {

	private final JwtProperties properties;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		String accessToken = jwtTokenProvider.resolveToken(request, JwtTokenType.ACCESS_TOKEN);
		JwtStatus accessTokenStatus = jwtTokenProvider.validateToken(accessToken);

		// 유효기간 검증하기
		switch (accessTokenStatus) {
			case ACCESS -> {
				// 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
				// 받은 유저 정보 : hglee67 아이디의 권한을 SpringSecurity에 저장함
				Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

				// SecurityContext 에 Authentication 객체를 저장합니다.
				return chain.filter(exchange)
						.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
			}
			case EXPIRED -> { // 만료 및 쿠키에서 삭제된 Access Token인 경우
				// Access Token이 만료되면, Refresh Token 유효한지 체크한
				// Refresh Token 확인하기
				String refreshToken = jwtTokenProvider.resolveToken(request, JwtTokenType.REFRESH_TOKEN);

				// Refresh Token 유효기간 검증하기
				JwtStatus refreshTokenStatus = jwtTokenProvider.validateToken(refreshToken);

				log.info("refreshTokenStatus : " + refreshTokenStatus);
				// Refresh Token이 유효하면, Access Token 재발급
				if (refreshTokenStatus == JwtStatus.ACCESS) {
					// Refresh Token에 저장된 정보 가져오기
					TokenDto tokenDto = jwtTokenProvider.getTokenInfo(refreshToken);

					// Access Token 재 발급
					String reAccessToken = jwtTokenProvider.createToken(tokenDto.userid(), tokenDto.role());

					// 만약, 기존 존재하는 Access Token있다면, 삭제
					response.addCookie(this.deleteTokenCookie(properties.getTokenAccessName()));
					// 재발급된 Access Token을 쿠키에 저장함
					response.addCookie(
							this.createTokenCookie(
									properties.getTokenAccessName(), properties.getTokenAccessValidTime(), reAccessToken));

					// 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
					// 받은 유저 정보 : hglee67 아이디의 권한을 SpringSecurity에 저장함
					Authentication authentication = jwtTokenProvider.getAuthentication(reAccessToken);

					return chain.filter(exchange)
							.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
				}
			}
			case DENIED -> log.info("Access Token 없음 - 스프링 시큐리티가 로그인 페이지로 이동 시킴");
		}
		return chain.filter(exchange);
	}

	private ResponseCookie deleteTokenCookie(String tokenName) {
		return ResponseCookie.from(tokenName, "")
				.maxAge(0)
				.build();
	}

	private ResponseCookie createTokenCookie(String tokenName, long tokenValidTime, String token) {
		return ResponseCookie.from(tokenName, token)
				.domain("localhost")
				.maxAge(tokenValidTime)
				.httpOnly(true)
				.secure(true)
				.sameSite("None")
				.path("/")
				.build();
	}
}
