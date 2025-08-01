package org.scoula.domain.notification;

import lombok.Getter;

@Getter
public enum NotiType {
	NOTICE,
	DEPOSIT,
	WITHDRAWAL,
	REQUEST,
	ACCEPTED,
	EXCHANGE,
	PAYED;
	// NOTICE("공지"),
	// DEPOSIT("입금 알림"),
	// WITHDRAWAL("출금 알림"),
	// REQUEST("입금 요청"),
	// ACCEPTED("요청 수락"),
	// EXCHANGE("환전 완료"),
	// PAYED("결제 완료");
	// 실제 Enum 값: 광고성, 입금요청, 출금알림 등 필요에 따라 수정 가능 --> 더 추가하기
}
