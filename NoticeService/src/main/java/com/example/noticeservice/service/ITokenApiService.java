package com.example.noticeservice.service;

import com.example.noticeservice.dto.TokenDto;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@RefreshScope
@FeignClient(name = "ITokenApiService", url = "${api.gateway}")
public interface ITokenApiService {

	@PostMapping(value = "user/getTokenInfo")
	TokenDto getTokenInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

}
