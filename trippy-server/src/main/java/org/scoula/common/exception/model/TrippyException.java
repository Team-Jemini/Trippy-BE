package org.scoula.common.exception.model;

import org.scoula.common.exception.enums.ErrorCode;

import lombok.Getter;

@Getter
public class TrippyException extends RuntimeException {
	private final ErrorCode errorCode;

	public TrippyException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
