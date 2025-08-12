package org.scoula.controller.notification.request;

import java.time.LocalDateTime;

import org.scoula.domain.notification.NotificationVO;

public record NotiDTO(
	Long notiId,
	String title,
	String content,
	String notiType,
	Long amount,
	LocalDateTime createdAt
) {
	public static NotiDTO from(NotificationVO vo) {
		return new NotiDTO(
			vo.getNotiId(),
			vo.getTitle(),
			vo.getContent(),
			vo.getNotiType().name(),
			vo.getAmount(),
			vo.getCreatedAt()
		);
	}
}
