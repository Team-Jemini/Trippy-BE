package org.scoula.mapper.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.scoula.domain.notification.NotificationVO;

@Mapper
public interface NotificationMapper {

	List<NotificationVO> findAllByUserId(Long userId);

	NotificationVO findByNotiId(Long notiId);

	void saveNotification(NotificationVO notice);

	@Select("SELECT NOW()")
	LocalDateTime selectNow();
}
