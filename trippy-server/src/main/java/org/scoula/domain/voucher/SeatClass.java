package org.scoula.domain.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeatClass {
	ECO("이코노미"), BIZ("비즈니스"), FIRST("퍼스트");
	private final String value;
}
