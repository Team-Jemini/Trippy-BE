package org.scoula.domain.transaction;

import lombok.Getter;

@Getter
public enum TransactionStatus {
	SUCCESS, PENDING, FAIL;
	// SUCCESS("성공"), PENDING("대기"), FAIL("실패");
}
