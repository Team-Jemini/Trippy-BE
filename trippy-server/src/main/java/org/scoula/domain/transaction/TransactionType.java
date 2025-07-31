package org.scoula.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {
	DEPOSIT("입금"), WITHDRAW("출금");
	private final String value;
}
