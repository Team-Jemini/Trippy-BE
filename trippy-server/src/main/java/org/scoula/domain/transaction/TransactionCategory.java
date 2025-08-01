package org.scoula.domain.transaction;

import lombok.Getter;

@Getter
public enum TransactionCategory {
	TRANSPORTATION,
	CULTURE,
	SHOPPING,
	FOOD,
	ACCOMMODATION,
	OTHER,
	INCOME;
	// TRANSPORTATION("교통"),
	// CULTURE("문화"),
	// SHOPPING("쇼핑"),
	// FOOD("식사"),
	// ACCOMMODATION("숙박"),
	// OTHER("기타"),
	// INCOME("수입");
}
