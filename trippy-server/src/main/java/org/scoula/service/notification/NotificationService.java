package org.scoula.service.notification;

import java.util.List;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.BadRequestException;
import org.scoula.controller.notification.request.NotiDTO;
import org.scoula.domain.notification.NotificationVO;
import org.scoula.mapper.notification.NotificationMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationMapper notificationMapper;

	public List<NotiDTO> getNotis(Long userId) {
		List<NotificationVO> notifications = notificationMapper.findAllByUserId(userId);

		return notifications.stream()
			.map(NotiDTO::from)
			.toList();
	}

	public NotiDTO getDetailNoti(Long notiId) {
		NotificationVO notification = notificationMapper.findByNotiId(notiId);

		if (notification == null) {
			throw new BadRequestException(ErrorCode.NOT_FOUND_NOTI_EXCEPTION);
		}

		return NotiDTO.from(notification);
	}
}
