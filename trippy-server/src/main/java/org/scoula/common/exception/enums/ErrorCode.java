package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	//400 BAD REQUEST

	//401 UNAUTHORIZED _인증
	TOKEN_NOT_CONTAINED_EXCEPTION(HttpStatus.UNAUTHORIZED, "Access Token이 필요합니다."),
	TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다. 다시 로그인 해주세요."),

	//404 NOT FOUND
	EXCHANGE_RATE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "환율 API 호출 중 오류 발생했습니다."),

	//405 METHOD_NOT_ALLOWED

	//409 CONFLICT

	//500
	INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
	EXCHANGE_RATE_SAVE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "환율 저장 중 오류 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}
}
