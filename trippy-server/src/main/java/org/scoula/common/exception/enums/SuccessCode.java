package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	REFRESH_SUCCESS(HttpStatus.OK, "토큰 갱신 성공입니다."),
    // 환율 정보 저장
	SAVE_EXCHANGE_RATE_SUCCESS(HttpStatus.OK, "환율 정보 저장 성공입니다.");

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
