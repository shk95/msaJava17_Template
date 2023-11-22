package com.example.userservice.controller;

import com.example.userservice.auth.AuthInfo;
import com.example.userservice.auth.JwtTokenProvider;
import com.example.userservice.auth.JwtTokenType;
import com.example.userservice.dto.TokenDto;
import com.example.userservice.dto.UserInfoDto;
import com.example.userservice.service.IUserInfoSsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
		origins = {"http://localhost:12000", "http://localhost:13000", "http://localhost:14000"},
		allowedHeaders = {"*"},
		methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
		allowCredentials = "true")
@Tag(name = "로그인된 사용자들이 접근하는 API", description = "로그인된 사용자들이 접근하는 API")
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserInfoController {

	private final JwtTokenProvider jwtTokenProvider;
	private final IUserInfoSsService userInfoSsService;

	@Operation(
			summary = "토큰에 저장된 사용자 정보를 조회하는 API.", description = "토큰에 저장된 사용자 정보를 조회하는 API.",
			responses = {
					@ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
					@ApiResponse(responseCode = "401", description = "사용자 정보 조회 실패")
			})
	@PostMapping("/getTokenInfo")
	private TokenDto getTokenInfo(HttpServletRequest request) {
		String accessToken = jwtTokenProvider.resolveToken(request, JwtTokenType.ACCESS);
		return jwtTokenProvider.getTokenInfo(accessToken);
	}

	@Operation(
			summary = "사용자 정보를 조회하는 API.", description = "사용자 정보를 조회하는 API.",
			responses = {
					@ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
					@ApiResponse(responseCode = "401", description = "사용자 정보 조회 실패")
			})
	@PostMapping("/userInfo")
	public UserInfoDto userInfo(@AuthenticationPrincipal AuthInfo authInfo) {
		return userInfoSsService.getUserInfo(authInfo.userInfoDto());
	}


}
