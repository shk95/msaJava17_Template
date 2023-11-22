package com.example.noticeservice.controller;

import com.example.noticeservice.dto.MsgDto;
import com.example.noticeservice.dto.NoticeDto;
import com.example.noticeservice.dto.TokenDto;
import com.example.noticeservice.service.INoticeService;
import com.example.noticeservice.service.ITokenApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
		origins = {"http://localhost:13000", "http://localhost:14000"},
		allowedHeaders = "*",
		allowCredentials = "true",
		methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.HEAD})
@Tag(name = "공지사항 서비스", description = "공지사항 관련 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notice")
@RestController
public class NoticeController {

	private final INoticeService noticeService;
	private final ITokenApiService tokenApiService;

	@Operation(summary = "공지사항 조회", description = "공지사항 조회",
			responses = {
					@ApiResponse(responseCode = "200", description = "공지사항 조회 성공"),
					@ApiResponse(responseCode = "404", description = "공지사항 조회 실패")
			})
	@PostMapping("/noticeList")
	public List<NoticeDto> noticeList() {
		return noticeService.getNoticeList();
	}

	@Operation(summary = "공지사항 상세보기", description = "공지사항 상세보기 결과 및 조회수 증가",
			parameters = {
					@Parameter(name = "nSeq", description = "공지사항 글번호"),
					@Parameter(name = "readCntYn", description = "조회수 증가 여부")
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "조회 성공"),
					@ApiResponse(responseCode = "404", description = "조회 실패")
			})
	@PostMapping("/noticeInfo")
	public NoticeDto noticeInfo(HttpServletRequest request) {
		Long nSeq = Long.parseLong(request.getParameter("nSeq"));
		boolean readCntYn = request.getParameter("readCntYn").equals("Y");
		return noticeService.getNoticeInfo(NoticeDto.builder().noticeSeq(nSeq).build(), readCntYn);
	}

	@Operation(summary = "공지사항 등록", description = "공지사항 등록",
			responses = {
					@ApiResponse(responseCode = "200", description = "등록 성공"),
					@ApiResponse(responseCode = "404", description = "등록 실패")
			})
	public MsgDto noticeInsert(HttpServletRequest request, @CookieValue(value = "${jwt.token.access.name") String token) {
		TokenDto tokenDto = tokenApiService.getTokenInfo(token);
		String title = request.getParameter("title");
		String content = request.getParameter("contents");
		String noticeYn = request.getParameter("noticeYn");

		NoticeDto noticeDto = NoticeDto.builder()
				.title(title)
				.content(content)
				.noticeYn(noticeYn)
				.regId(tokenDto.userId())
				.build();

		noticeService.insertNoticeInfo(noticeDto);
		return MsgDto.builder().result(1).msg("등록 성공").build();
	}

	@Operation(summary = "공지사항 수정", description = "공지사항 수정",
			responses = {
					@ApiResponse(responseCode = "200", description = "수정 성공"),
					@ApiResponse(responseCode = "404", description = "수정 실패")
			})
	@PostMapping("/noticeUpdate")
	public MsgDto noticeUpdate(HttpServletRequest request, @CookieValue(value = "${jwt.token.access.name}") String token) {
		TokenDto tokenDto = tokenApiService.getTokenInfo(token);
		Long nSeq = Long.parseLong(request.getParameter("nSeq"));
		String title = request.getParameter("title");
		String content = request.getParameter("contents");
		String noticeYn = request.getParameter("noticeYn");

		NoticeDto noticeDto = NoticeDto.builder()
				.noticeSeq(nSeq)
				.title(title)
				.content(content)
				.noticeYn(noticeYn)
				.userId(tokenDto.userId())
				.build();

		noticeService.updateNoticeInfo(noticeDto);
		return MsgDto.builder().result(1).msg("수정 성공").build();
	}

	@Operation(summary = "공지사항 삭제", description = "공지사항 삭제",
			responses = {
					@ApiResponse(responseCode = "200", description = "삭제 성공"),
					@ApiResponse(responseCode = "404", description = "삭제 실패")
			})
	@PostMapping("/noticeDelete")
	public MsgDto noticeDelete(HttpServletRequest request) {
		Long nSeq = Long.parseLong(request.getParameter("nSeq"));
		noticeService.deleteNoticeInfo(NoticeDto.builder().noticeSeq(nSeq).build());
		return MsgDto.builder().result(1).msg("삭제 성공").build();
	}
}
