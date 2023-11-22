package com.example.noticeservice.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record MsgDto(
		int result, // 결과 코드
		String msg // 결과 메시지
) {

}
