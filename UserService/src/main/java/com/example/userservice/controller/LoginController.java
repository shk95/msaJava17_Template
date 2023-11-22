package com.example.userservice.controller;

import com.example.userservice.auth.AuthInfo;
import com.example.userservice.auth.JwtTokenProvider;
import com.example.userservice.auth.JwtTokenType;
import com.example.userservice.config.Properties;
import com.example.userservice.dto.MsgDto;
import com.example.userservice.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
		origins = {"http://localhost:13000", "http://localhost:14000"},
		allowedHeaders = {"*"},
		allowCredentials = "true",
		methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

	private final Properties properties;
	private final JwtTokenProvider jwtTokenProvider;

	@Operation(summary = "로그인 성공처리 API", description = "로그인 성공시 호출되는 API",
			responses = {
					@ApiResponse(responseCode = "200", description = "로그인 성공"),
					@ApiResponse(responseCode = "404", description = "로그인 실패")
			})
	@PostMapping("/loginSuccess")
	public MsgDto loginSuccess(@AuthenticationPrincipal AuthInfo authInfo, HttpServletResponse response) {
		UserInfoDto userInfoDto = authInfo.userInfoDto();

		String accessToken = jwtTokenProvider.createToken(userInfoDto.userid(), userInfoDto.roles(), JwtTokenType.ACCESS);
		ResponseCookie cookie = ResponseCookie.from(properties.getTokenAccessName(), accessToken)
				.domain("localhost")
				.path("/")
				.secure(true)
				.sameSite("None")
				.maxAge(properties.getTokenAccessValidTime())
				.httpOnly(true).build();
		response.setHeader("Set-Cookie", cookie.toString());

		String refreshToken = jwtTokenProvider.createToken(userInfoDto.userid(), userInfoDto.roles(), JwtTokenType.REFRESH);
		cookie = ResponseCookie.from(properties.getTokenRefreshName(), refreshToken)
				.domain("localhost")
				.path("/")
				.secure(true)
				.sameSite("None")
				.maxAge(properties.getTokenRefreshValidTime())
				.httpOnly(true).build();
		response.addHeader("Set-Cookie", cookie.toString());

		return MsgDto.builder().result(1).msg(userInfoDto.username() + " 님 로그인이 성공하였습니다.").build();
	}

	@Operation(summary = "로그인 실패처리 API", description = "로그인 실패시 호출되는 API",
			responses = {
					@ApiResponse(responseCode = "200", description = "로그인 실패"),
					@ApiResponse(responseCode = "404", description = "응답 실패")
			})
	@PostMapping("/loginFail")
	public MsgDto loginFail() {
		return MsgDto.builder().result(1).msg("로그인이 실패하였습니다.").build();
	}


}
