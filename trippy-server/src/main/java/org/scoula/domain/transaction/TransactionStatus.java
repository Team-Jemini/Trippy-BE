package org.scoula.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionStatus {
	SUCCESS("성공"), PENDING("대기"), FAIL("실패");
	private final String value;
}
