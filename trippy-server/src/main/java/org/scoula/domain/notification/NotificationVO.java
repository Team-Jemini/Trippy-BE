package org.scoula.domain.notification;

import org.scoula.domain.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class NotificationVO extends BaseTime {
	private Long userId;
	private Long notiId;
	private String title;
	private String content;
	private NotiType notiType;
	private Long amount;
}