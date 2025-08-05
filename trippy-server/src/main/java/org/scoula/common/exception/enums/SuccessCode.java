package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	REFRESH_SUCCESS(HttpStatus.OK, "토큰 갱신 성공입니다."),
	RESIDENT_CARD_OCR_SUCCESS(HttpStatus.OK, "주민등록증 정보 인식이 완료되었습니다."),
	FIND_AIR_TICKET_SUCCESS(HttpStatus.OK, "항공권 전체조회 성공"),
	CREATE_GROUP_ACCOUNT_SUCCESS(HttpStatus.OK, "모임 계좌가 성공적으로 생성되었습니다."),
	CREATE_INVITE_TOKEN_SUCCESS(HttpStatus.OK, "초대 링크가 성공적으로 생성 되었습니다."),
	PARSE_INVITE_TOKEN_SUCCESS(HttpStatus.OK, "초대 토큰을 성공적으로 파싱했습니다."),
	;

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
