package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	//토큰
	REFRESH_SUCCESS(HttpStatus.OK, "토큰 갱신 성공입니다."),
	SIGNUP_SUCCESS(HttpStatus.OK, "회원가입 성공"),
	CHECK_PASSWORD_SUCCESS(HttpStatus.OK, "비밀번호 확인 성공"),
	GET_ALL_USERS_SUCCESS(HttpStatus.OK, "유저 토큰 전체 조회 성공"),

	//알림
	GET_ALL_NOTIS_SUCCESS(HttpStatus.OK, "알림 전체 조회 성공"),
	GET_DETAIL_NOTI_SUCCESS(HttpStatus.OK, "알림 상세 조회 성공"),

	//환율
	SAVE_EXCHANGE_RATE_SUCCESS(HttpStatus.OK, "환율 정보 저장 성공입니다."),
	FIND_EXCHANGE_RATE_SUCCESS(HttpStatus.OK, "환율 정보 찾기 성공"),
	FIND_EXCHANGE_BALANCE_SUCCESS(HttpStatus.OK, "환율 잔액 찾기 성공"),
	FIND_ACCOUNT_LIST_SUCCESS(HttpStatus.OK, "계좌 리스트 찾기 성공"),
	EXCHANGE_SUCCESS(HttpStatus.OK, "환전 거래 성공"),

	//주민등록, 여권
	RESIDENT_CARD_OCR_SUCCESS(HttpStatus.OK, "주민등록증 정보 인식이 완료되었습니다."),
	RESIDENT_CARD_SUCCESS(HttpStatus.OK, "주민등록증 조회를 성공하였습니다."),
	RESIDENT_CARD_ADD_SUCCESS(HttpStatus.OK, "주민등록증 등록에 성공하였습니다."),
	PASSPORT_GET_SUCCESS(HttpStatus.OK, "여권 조회 성공"),
	PASSPORT_ADD_SUCCESS(HttpStatus.OK, "여권 등록 성공"),

	//카드
	FIND_CODEF_CARD_SUCCESS(HttpStatus.OK, "CODEF 카드 정보 저장 완료"),
	FIND_CODEF_CARD_QUERY_SUCCESS(HttpStatus.OK, "CODEF 카드 정보 조회 성공"),
	DELETE_CARD_SUCCESS(HttpStatus.OK, "카드 삭제 성공"),
	UPDATE_CARD_NICKNAME_SUCCESS(HttpStatus.OK, "카드 별명 등록 성공"),
	SET_MAIN_CARD_SUCCESS(HttpStatus.OK, "주카드 설정이 완료되었습니다."),
	QR_CODE_SUCCESS(HttpStatus.OK, "QR코드가 생성되었습니다"),

	//항공권, 숙소, 관광
	FIND_AIR_TICKET_SUCCESS(HttpStatus.OK, "항공권 전체조회 성공"),
	FIND_DETAIL_AIR_TICKET_SUCCESS(HttpStatus.OK, "항공권 상세조회 성공"),
	FIND_VOUCHER_SUCCESS(HttpStatus.OK, "바우처(숙소,관광) 전체조회 성공"),
	FIND_DETAIL_ACCOMMODATION_SUCCESS(HttpStatus.OK, "숙소예약 상세조회 성공"),
	CREATE_SIGHTSEEING_SUCCESS(HttpStatus.OK, "관광예약 생성 성공"),

	//CODEF
	GET_CODEF_DATA_SUCCESS(HttpStatus.OK, "내 보유 계좌 Codef 조회 성공"),
	SAVE_ACCOUNT_DATA_SUCCESS(HttpStatus.OK, "내 계좌 등록 성공"),

	//모임
	CREATE_GROUP_ACCOUNT_SUCCESS(HttpStatus.OK, "모임 계좌가 성공적으로 생성되었습니다."),
	CREATE_INVITE_TOKEN_SUCCESS(HttpStatus.OK, "초대 링크가 성공적으로 생성 되었습니다."),
	PARSE_INVITE_TOKEN_SUCCESS(HttpStatus.OK, "초대 토큰을 성공적으로 파싱했습니다."),
	SETTLE_GROUP_ACCOUNT_SUCCESS(HttpStatus.OK, "모임계좌 정산 요청 성공"),
	JOIN_GROUP_ACCOUNT_SUCCESS(HttpStatus.OK, "모임계좌에 가입 성공"),
	GET_GROUP_ACCOUNT_DETAIL_SUCCESS(HttpStatus.OK, "모임 계좌 상세 조회 성공입니다."),
	FIND_GROUP_ACCOUNT_MEMBER_SUCCESS(HttpStatus.OK, "모임 계좌 멤버 조회 성공"),

	//계좌
	GET_PERSONAL_ACCOUNT_DETAIL_SUCCESS(HttpStatus.OK, "개인계좌 상세 조회 성공"),
	FILTER_ACCOUNT_TRANSACTION_SUCCESS(HttpStatus.OK, "계좌 거래 내역 조회 성공"),
	FIND_ACCOUNTS_LIST_SUCCESS(HttpStatus.OK, "계좌 목록 조회 성공"),
	TRANSFER_SUCCESS(HttpStatus.OK, "송금이 성공적으로 완료되었습니다."),

	//여행로그
	FIND_TRAVEL_LOG_SUCCESS(HttpStatus.OK, "여행 로그 조회 성공"),
	FIND_TRAVEL_REPORT_SUCCESS(HttpStatus.OK, "여행 리포트 조회 성공"),
	CREATE_TRAVEL_REPORT_SUCCESS(HttpStatus.OK, "여행 리포트 생성 성공"),
	CHECK_TRAVEL_DATE_AVAILABLE_SUCCESS(HttpStatus.OK, "여행 로그 생성 가능 여부"),
	CREATE_TRAVEL_LOG_SUCCESS(HttpStatus.OK, "여행 로그 생성 성공");


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
