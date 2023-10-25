package kopo.poly.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import kopo.poly.auth.AuthInfo;
import kopo.poly.auth.JwtTokenProvider;
import kopo.poly.auth.JwtTokenType;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@CrossOrigin(origins = {"http://localhost:13000", "http://localhost:14000"},
        allowedHeaders = {"POST, GET"},
        allowCredentials = "true")
@Tag(name = "로그인 안된 요청들이 접근하는 서비스", description = "로그인 성공, 실패 API")
@Slf4j
@RequestMapping(value = "/login")
@RequiredArgsConstructor
@RestController
public class LoginController {

    @Value("${jwt.token.access.valid.time}")
    private long accessTokenValidTime;

    @Value("${jwt.token.access.name}")
    private String accessTokenName;

    @Value("${jwt.token.refresh.valid.time}")
    private long refreshTokenValidTime;

    @Value("${jwt.token.refresh.name}")
    private String refreshTokenName;

    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "로그인 성공처리  API", description = "로그인 성공처리 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Page Not Found!"),
            }
    )
    @PostMapping(value = "loginSuccess")
    public MsgDTO loginSuccess(@AuthenticationPrincipal AuthInfo authInfo,
                               HttpServletResponse response) {

        log.info(this.getClass().getName() + ".loginSuccess Start!");

        // Spring Security에 저장된 정보 가져오기
        UserInfoDTO rDTO = Optional.ofNullable(authInfo.getUserInfoDTO()).orElseGet(UserInfoDTO::new);

        String userId = CmmUtil.nvl(rDTO.getUserId());
        String userName = CmmUtil.nvl(rDTO.getUserName());
        String userRoles = CmmUtil.nvl(rDTO.getRoles());

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("userRoles : " + userRoles);

        // Access Token 생성
        String accessToken = jwtTokenProvider.createToken(userId, userRoles, JwtTokenType.ACCESS_TOKEN);
        log.info("accessToken : " + accessToken);


        ResponseCookie cookie = ResponseCookie.from(accessTokenName, accessToken)
                .domain("localhost")
                .path("/")
//                .secure(true)
//                .sameSite("None")
                .maxAge(accessTokenValidTime) // JWT Refresh Token 만료시간 설정
                .httpOnly(true)
                .build();

        // 기존 쿠기 모두 삭제하고, Cookie에 Access Token 저장하기
        response.setHeader("Set-Cookie", cookie.toString());

        cookie = null;

        // Refresh Token 생성
        // Refresh Token은 보안상 노출되면, 위험하기에 Refresh Token은 DB에 저장하고,
        // DB를 조회하기 위한 값만 Refresh Token으로 생성함
        // 본 실습은 DB에 저장하지 않고, 사용자 컴퓨터의 쿠키에 저장함
        // Refresh Token은 Access Token에 비해 만료시간을 길게 설정함
        String refreshToken = jwtTokenProvider.createToken(userId, userRoles, JwtTokenType.REFRESH_TOKEN);

        log.info("refreshToken : " + refreshToken);

        cookie = ResponseCookie.from(refreshTokenName, refreshToken)
                .domain("localhost")
                .path("/")
//                .secure(true)
//                .sameSite("None")
                .maxAge(refreshTokenValidTime) // JWT Refresh Token 만료시간 설정
                .httpOnly(true)
                .build();

//         기존 쿠기에 Refresh Token 저장하기
        response.addHeader("Set-Cookie", cookie.toString());

        // 결과 메시지 전달하기
        MsgDTO dto = new MsgDTO();
        dto.setResult(1);
        dto.setMsg(userName + "님 로그인이 성공하였습니다.");

        log.info(this.getClass().getName() + ".loginSuccess End!");

        return dto;

    }

    @Operation(summary = "로그인 실패처리  API", description = "로그인 실패처리 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Page Not Found!"),
            }
    )
    @PostMapping(value = "loginFail")
    public MsgDTO loginFail() {

        log.info(this.getClass().getName() + ".loginFail Start!");

        // 결과 메시지 전달하기
        MsgDTO dto = new MsgDTO();
        dto.setResult(0);
        dto.setMsg("로그인이 실패하였습니다.");

        log.info(this.getClass().getName() + ".loginFail End!");

        return dto;

    }

}
