package org.scoula.domain.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeType {
	sell("외화 판매"), buy("외화 구매");
	private final String value;
}
