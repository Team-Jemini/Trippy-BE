package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	FIND_AIR_TICKET_SUCCESS(HttpStatus.OK, "항공권 전체조회 성공"),
	REFRESH_SUCCESS(HttpStatus.OK, "토큰 갱신 성공입니다."),
	GET_CODEF_DATA_SUCCESS(HttpStatus.OK, "Codef 데이터 조회 성공");

	private final HttpStatus httpStatus;
	private final String message;

	SuccessCode(HttpStatus httpStatus, String message) {
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
