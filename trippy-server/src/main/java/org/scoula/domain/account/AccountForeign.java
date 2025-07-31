package org.scoula.domain.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountForeign {
	kor("국내계좌"), foreigner("외화계좌");
	private final String value;
}
