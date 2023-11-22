package com.example.noticeservice.service;

import com.example.noticeservice.dto.NoticeDto;

import java.util.List;

public interface INoticeService {

	List<NoticeDto> getNoticeList();

	NoticeDto getNoticeInfo(NoticeDto noticeDto, boolean type);

	void updateNoticeInfo(NoticeDto noticeDto);

	void deleteNoticeInfo(NoticeDto noticeDto);

	void insertNoticeInfo(NoticeDto noticeDto);
}
