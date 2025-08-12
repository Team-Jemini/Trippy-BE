package org.scoula.controller.notification.request;

import org.scoula.domain.notification.NotificationVO;

public record NotiDTO(
	Long notiId,
	String title,
	String content,
	String notiType,
	Long amount
) {
	public static NotiDTO from(NotificationVO vo) {
		return new NotiDTO(
			vo.getNotiId(),
			vo.getTitle(),
			vo.getContent(),
			vo.getNotiType().name(),
			vo.getAmount()
		);
	}
}
