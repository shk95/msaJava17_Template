package com.example.userservice.controller;

import com.example.userservice.auth.UserRole;
import com.example.userservice.dto.MsgDto;
import com.example.userservice.dto.UserInfoDto;
import com.example.userservice.service.IUserInfoSsService;
import com.example.userservice.util.EncryptUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
		origins = {"http://localhost:13000", "http://localhost:14000"},
		allowedHeaders = {"*"},
		methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
		allowCredentials = "true")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/login")
@RestController
public class UserRegController {

	private final IUserInfoSsService userInfoSsService;
	private final PasswordEncoder bCryptPasswordEncoder;

	@Operation(summary = "회원가입  API", description = "회원가입 API",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK"),
					@ApiResponse(responseCode = "404", description = "Page Not Found!"),
			}
	)
	@PostMapping(value = "insertUserInfo")
	public MsgDto insertUserInfo(HttpServletRequest request) throws Exception {
		String userId = request.getParameter("user_id"); //아이디
		String userName = request.getParameter("user_name"); //이름
		String password = request.getParameter("password"); //비밀번호
		String email = request.getParameter("email"); //이메일
		String addr1 = request.getParameter("addr1"); //주소
		String addr2 = request.getParameter("addr2"); //상세주소

		UserInfoDto userInfoDto = UserInfoDto.builder()
				.userid(userId)
				.username(userName)
				.password(bCryptPasswordEncoder.encode(password))
				.email(EncryptUtil.encAES128CBC(email))
				.addr1(addr1)
				.addr2(addr2)
				.roles(UserRole.USER.getValue()).build();

		boolean res = userInfoSsService.insertUserInfo(userInfoDto);
		return MsgDto.builder().result(res ? 1 : 0).msg(res ? "가입되었습니다." : "가입에 실패하였습니다.").build();
	}
}
