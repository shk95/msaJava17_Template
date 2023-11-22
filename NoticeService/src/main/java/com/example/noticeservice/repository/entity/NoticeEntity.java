package com.example.noticeservice.repository.entity;

import com.example.noticeservice.dto.NoticeDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "NOTICE")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class NoticeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "NOTICE_SEQ")
	private Long noticeSeq;

	@NotNull
	@Column(name = "NOTICE_TITLE", length = 500, nullable = false)
	private String title;

	@NotNull
	@Column(name = "NOTICE_YN", length = 1, nullable = false)
	private boolean noticeYn;

	@NotNull
	@Column(name = "NOTICE_CONTENT", nullable = false)
	private String content;

	@NotNull
	@Column(name = "NOTICE_USER_ID", nullable = false)
	private String userId;

	@Column(name = "NOTICE_RCNT", nullable = false)
	private Long readCnt;

	@Column(name = "NOTICE_REG_ID", updatable = false)
	private String regId;

	@Column(name = "NOTICE_REG_DT", updatable = false)
	private String regDt;

	@Column(name = "NOTICE_CHG_IT")
	private String chgId;

	@Column(name = "NOTICE_CHG_DT")
	private String chgDt;

	public void updateContent(NoticeDto noticeDto) {
		this.title = noticeDto.title();
		this.noticeYn = noticeDto.noticeYn().equals("Y");
		this.content = noticeDto.content();
		this.userId = noticeDto.userId();
	}
}
