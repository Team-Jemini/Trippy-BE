package org.scoula.common.dto;

import org.scoula.common.exception.enums.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
// @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
	private final int code;
	private final String message;

	public ErrorResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ErrorResponse error(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
	}

	public static ErrorResponse badRequestError(final String errorMessage) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
	}
}
