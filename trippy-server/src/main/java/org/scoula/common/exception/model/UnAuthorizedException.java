package org.scoula.common.exception.model;

import org.scoula.common.exception.enums.ErrorCode;

public class UnAuthorizedException extends TrippyException {
	public UnAuthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
