package org.scoula.domain.transaction;

import lombok.Getter;

@Getter
public enum TransactionType {
	DEPOSIT, WITHDRAW;
	// DEPOSIT("입금"), WITHDRAW("출금");
}
