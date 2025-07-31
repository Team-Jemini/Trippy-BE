package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	//400 BAD REQUEST

	//401 UNAUTHORIZED _인증
	TOKEN_NOT_CONTAINED_EXCEPTION(HttpStatus.UNAUTHORIZED, "Access Token이 필요합니다."),
	TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다. 다시 로그인 해주세요."),

	//404 NOT FOUND

	//405 METHOD_NOT_ALLOWED

	//409 CONFLICT

	//500
	INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
