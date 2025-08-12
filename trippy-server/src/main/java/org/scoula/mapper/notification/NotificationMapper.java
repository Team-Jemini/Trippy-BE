package org.scoula.mapper.notification;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.notification.NotificationVO;
import org.springframework.data.repository.query.Param;

@Mapper
public interface NotificationMapper {

	List<NotificationVO> findAllByUserId(Long userId);
	NotificationVO findByNotiId(Long notiId);
}
