package com.example.noticeservice.dto;

import com.example.noticeservice.repository.entity.NoticeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record NoticeDto(
		Long noticeSeq, // 기본키, 순번
		String title, // 제목
		String noticeYn, // 공지글 여부
		String content, // 글 내용
		String userId, // 작성자
		String readCnt, // 조회수
		String regId, // 등록자 아이디
		String regDt, // 등록일
		String chgId, // 수정자 아이디
		String chgDt, // 수정일
		String userName // 등록자명

) {

	public static NoticeEntity toEntity(NoticeDto noticeDto) {
		return NoticeEntity.builder()
				.noticeSeq(noticeDto.noticeSeq())
				.title(noticeDto.title())
				.noticeYn(noticeDto.noticeYn().equals("Y"))
				.content(noticeDto.content())
				.userId(noticeDto.userId())
				.readCnt(Long.parseLong(noticeDto.readCnt()))
				.regId(noticeDto.regId())
				.regDt(noticeDto.regDt())
				.chgId(noticeDto.chgId())
				.chgDt(noticeDto.chgDt())
				.build();
	}

	public static NoticeDto toDto(NoticeEntity noticeEntity) {
		return NoticeDto.builder()
				.noticeSeq(noticeEntity.getNoticeSeq())
				.title(noticeEntity.getTitle())
				.noticeYn(noticeEntity.isNoticeYn() ? "Y" : "N")
				.content(noticeEntity.getContent())
				.userId(noticeEntity.getUserId())
				.readCnt(String.valueOf(noticeEntity.getReadCnt()))
				.regId(noticeEntity.getRegId())
				.regDt(noticeEntity.getRegDt())
				.chgId(noticeEntity.getChgId())
				.chgDt(noticeEntity.getChgDt())
				.build();
	}
}
