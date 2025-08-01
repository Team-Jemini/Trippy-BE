package org.scoula.domain.account;

import lombok.Getter;

@Getter
public enum AccountForeign {
	kor, foreigner;
	// kor("국내계좌"), foreigner("외화계좌");
}
