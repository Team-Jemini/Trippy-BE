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
	USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
	CONNECTED_ID_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "connectedId를 찾을 수 없습니다."),
	//405 METHOD_NOT_ALLOWED

	//409 CONFLICT

	//500
	INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
	FILE_PROCESSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리 오류입니다."),
	PARSING_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파싱 실패 오류입니다."),
	ACCOUNT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "모임계좌 생성 실패: 시도 5회 초과"),
	ACCOUNT_ID_DUPLICATION(HttpStatus.INTERNAL_SERVER_ERROR, "계좌 번호 중복: 시도 5회 초과"),
	DUPLICATE_ACCOUNT_ID_EXCEPTION(HttpStatus.CONFLICT, "이미 존재하는 계좌번호입니다."),
	ACCESS_TOKEN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AccessToken 발급 실패"),
	CARD_1_PROCESS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카드1 처리 실패"),
	CARD_2_PROCESS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카드2 처리 실패");
	;

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
