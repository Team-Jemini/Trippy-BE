package org.scoula.common.exception.model;

import org.scoula.common.exception.enums.ErrorCode;

public class NotFoundException extends TrippyException {
	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

}
