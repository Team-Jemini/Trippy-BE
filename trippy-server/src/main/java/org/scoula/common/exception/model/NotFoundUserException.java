package org.scoula.common.exception.model;

import org.scoula.common.dto.TokenPair;
import org.scoula.common.exception.enums.ErrorCode;

import lombok.Getter;

@Getter
public class NotFoundUserException extends TrippyException {
	private final TokenPair tokenPair;

	public NotFoundUserException(ErrorCode errorCode, TokenPair tokenPair) {
		super(errorCode);
		this.tokenPair = tokenPair;
	}
}
