package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	//400 BAD REQUEST
	INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	INVALID_INVITE_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 링크입니다."),
	EXPIRED_INVITE_TOKEN(HttpStatus.BAD_REQUEST, "초대 수락 가능 기간이 지났습니다."),

	//401 UNAUTHORIZED _인증
	TOKEN_NOT_CONTAINED_EXCEPTION(HttpStatus.UNAUTHORIZED, "Access Token이 필요합니다."),
	TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다. 다시 로그인 해주세요."),

	//404 NOT FOUND
	ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계좌입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

	//405 METHOD_NOT_ALLOWED

	//409 CONFLICT
	ALREADY_INVITED(HttpStatus.CONFLICT, "이미 초대된 사용자입니다."),

	//500
	INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

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
