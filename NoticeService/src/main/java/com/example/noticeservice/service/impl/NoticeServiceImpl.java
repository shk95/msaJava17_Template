package com.example.noticeservice.service.impl;

import com.example.noticeservice.repository.NoticeRepository;
import com.example.noticeservice.repository.entity.NoticeEntity;
import com.example.noticeservice.dto.NoticeDto;
import com.example.noticeservice.service.INoticeService;
import com.example.noticeservice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements INoticeService {

	private final NoticeRepository noticeRepository;

	@Transactional(readOnly = true)
	@Override
	public List<NoticeDto> getNoticeList() {
		return noticeRepository.findAllByOrderByNoticeSeqDesc().stream()
				.map(NoticeDto::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public NoticeDto getNoticeInfo(NoticeDto noticeDto, boolean type) {
		Long seq = noticeDto.noticeSeq();
		return noticeRepository.findByNoticeSeq(seq)
				.map(entity -> {
					if (type) {
						noticeRepository.updateReadCount(seq);
						noticeRepository.flush();
					}
					return entity;
				})
				.map(NoticeDto::toDto)
				.orElse(null);
	}

	@Transactional
	@Override
	public void updateNoticeInfo(NoticeDto noticeDto) {
		noticeRepository.findByNoticeSeq(noticeDto.noticeSeq())
				.ifPresent(entity -> entity.updateContent(noticeDto));
	}

	@Transactional
	@Override
	public void deleteNoticeInfo(NoticeDto noticeDto) {
		noticeRepository.deleteById(noticeDto.noticeSeq());
	}

	@Transactional
	@Override
	public void insertNoticeInfo(NoticeDto noticeDto) {
		noticeRepository.save(NoticeEntity.builder()
				.title(noticeDto.title())
				.noticeYn(noticeDto.noticeYn().equals("Y"))
				.content(noticeDto.content())
				.userId(noticeDto.userId())
				.readCnt(0L)
				.regId(noticeDto.userId())
				.regDt(DateUtil.getDateTime("YYYY-MM-DD HH:mm:ss"))
				.chgDt(DateUtil.getDateTime("YYYY-MM-DD HH:mm:ss"))
				.build());
	}
}
