package org.scoula.common.exception.model;

import org.scoula.common.exception.enums.ErrorCode;

public class BadRequestException extends TrippyException {
	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
