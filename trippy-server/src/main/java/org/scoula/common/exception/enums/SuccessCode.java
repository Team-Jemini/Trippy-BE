package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	//토큰
	REFRESH_SUCCESS(HttpStatus.OK, "토큰 갱신 성공입니다."),

	//환율
	SAVE_EXCHANGE_RATE_SUCCESS(HttpStatus.OK, "환율 정보 저장 성공입니다."),
	FIND_EXCHANGE_RATE_SUCCESS(HttpStatus.OK, "환율 정보 찾기 성공"),
	FIND_EXCHANGE_BALANCE_SUCCESS(HttpStatus.OK, "환율 잔액 찾기 성공"),

	//주민등록, 여권
	RESIDENT_CARD_OCR_SUCCESS(HttpStatus.OK, "주민등록증 정보 인식이 완료되었습니다."),
	RESIDENT_CARD_SUCCESS(HttpStatus.OK, "주민등록증 조회를 성공하였습니다."),


	//항공권, 숙소, 관광
	FIND_AIR_TICKET_SUCCESS(HttpStatus.OK, "항공권 전체조회 성공"),

	FIND_CODEF_CARD_SUCCESS(HttpStatus.OK, "CODEF 카드 정보 저장 완료"),

	FIND_DETAIL_AIR_TICKET_SUCCESS(HttpStatus.OK, "항공권 상세조회 성공"),
	FIND_VOUCHER_SUCCESS(HttpStatus.OK, "바우처(숙소,관광) 전체조회 성공"),
	FIND_DETAIL_ACCOMMODATION_SUCCESS(HttpStatus.OK, "숙소예약 상세조회 성공"),
	CREATE_SIGHTSEEING_SUCCESS(HttpStatus.OK, "관광예약 생성 성공"),

	//모임
	CREATE_GROUP_ACCOUNT_SUCCESS(HttpStatus.OK, "모임 계좌가 성공적으로 생성되었습니다."),
	CREATE_INVITE_TOKEN_SUCCESS(HttpStatus.OK, "초대 링크가 성공적으로 생성 되었습니다."),
	PARSE_INVITE_TOKEN_SUCCESS(HttpStatus.OK, "초대 토큰을 성공적으로 파싱했습니다."),

	//여행로그
	FIND_TRAVEL_LOG_SUCCESS(HttpStatus.OK, "여행 로그 조회 성공");


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
