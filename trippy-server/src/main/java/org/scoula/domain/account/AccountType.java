package org.scoula.domain.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {
	person("개인"), group("모임");
	private final String value;
}
