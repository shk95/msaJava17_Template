package com.example.noticeservice.repository;

import com.example.noticeservice.repository.entity.NoticeEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

	List<NoticeEntity> findAllByOrderByNoticeSeqDesc();

	Optional<NoticeEntity> findByNoticeSeq(Long noticeSeq);

	@Modifying(clearAutomatically = true)
	@Query(value = """
			UPDATE NOTICE n SET n.NOTICE_RCNT = IFNULL(n.NOTICE_RCNT, 0)+1
			WHERE n.NOTICE_SEQ = :noticeSeq
			""", nativeQuery = true)
	int updateReadCount(@Param("noticeSeq") Long noticeSeq);
}
