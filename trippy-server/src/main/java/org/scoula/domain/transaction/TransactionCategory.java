package org.scoula.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionCategory {
	TRANSPORTATION("교통"),
	CULTURE("문화"),
	SHOPPING("쇼핑"),
	FOOD("식사"),
	ACCOMMODATION("숙박"),
	OTHER("기타"),
	INCOME("수입");
	private final String value;
}
