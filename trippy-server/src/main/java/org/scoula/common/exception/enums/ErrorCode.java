package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	//400 BAD REQUEST
	INVALID_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰을 입력했습니다."),
	INVALID_REFRESH_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰을 입력했습니다."),
	INVALID_TOKEN_TYPE_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 토큰 타입입니다. Access Token을 사용해주세요."),
	INVALID_PHONE_NUMBER_EXCEPTION(HttpStatus.BAD_REQUEST, "전화번호가 잘못되었습니다."),
	NOT_MATCH_VERIFICATION_CODE_EXCEPTION(HttpStatus.BAD_REQUEST, "전화번호 인증 코드가 일치하지 않습니다."),
	INVALID_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호가 잘못되었습니다."),

	SIGHTSEEING_BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "관광바우처 정보가 잘못되었습니다."),
	INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	INVALID_INVITE_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 링크입니다."),
	EXPIRED_INVITE_TOKEN(HttpStatus.BAD_REQUEST, "초대 수락 가능 기간이 지났습니다."),
	NOT_GROUP_ACCOUNT(HttpStatus.BAD_REQUEST, "모임계좌가 아닌 계좌입니다."),
	ACCOUNT_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "해지된 계좌입니다."),
	NOT_GROUP_ACCOUNT_LEADER_EXCEPTION(HttpStatus.BAD_REQUEST, "모임계좌 모임주가 아닌 사용자입니다."),

	INVALID_RESIDENT_NUMBER_EXCEPTION(HttpStatus.BAD_REQUEST, "주민번호가 잘못되었습니다."),
	ACCOUNT_NOT_USER_MAIN_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자의 대표계좌가 아닌 계좌입니다."),
	TRAVEL_ID_REQUIRED(HttpStatus.BAD_REQUEST, "travelId는 필수입니다."),
	TRAVEL_LOG_ACCOUNT_ID_EMPTY(HttpStatus.BAD_REQUEST, "travel_log.account_id가 비어 있습니다. travelId={0}"),

	//401 UNAUTHORIZED _인증
	TOKEN_NOT_CONTAINED_EXCEPTION(HttpStatus.UNAUTHORIZED, "Access Token이 필요합니다."),
	TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다. 다시 로그인 해주세요."),
	BLACKLISTED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 엑세스 토큰입니다."),

	//404 NOT FOUND
	ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계좌입니다."),
	CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카드입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
	USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
	NOT_FOUND_VERIFICATION_CODE_EXCEPTION(HttpStatus.BAD_REQUEST, "존재하지 않는 전화번호 인증 코드입니다."),

	CONNECTED_ID_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "connectedId를 찾을 수 없습니다."),

	EXCHANGE_RATE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "환율 API 호출 중 오류 발생했습니다."),
	AIR_TICKET_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 항공권Id가 존재하지 않습니다."),
	ACCOMMODATION_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 숙소가 존재하지 않습니다."), //관광 바우처 이미지가 잘못되었습니다
	TRAVEL_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 travelId가 존재하지 않습니다."),
	NOT_FOUND_NOTI_EXCEPTION(HttpStatus.BAD_REQUEST, "존재하지 않는 알람입니다."),

	//405 METHOD_NOT_ALLOWED

	//409 CONFLICT
	ALREADY_INVITED(HttpStatus.CONFLICT, "이미 가입된 사용자입니다."),

	//500
	INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
	FILE_PROCESSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리 오류입니다."),
	PARSING_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파싱 실패 오류입니다."),
	ACCOUNT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "모임계좌 생성 실패: 시도 5회 초과"),
	ACCOUNT_ID_DUPLICATION(HttpStatus.INTERNAL_SERVER_ERROR, "계좌 번호 중복: 시도 5회 초과"),
	DUPLICATE_ACCOUNT_ID_EXCEPTION(HttpStatus.CONFLICT, "이미 존재하는 계좌번호입니다."),
	CREATE_CONNECTED_ID_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "은행 인증 ID 생성에 실패했습니다."),
	GET_ACCOUNTS_LIST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "계좌 목록 조회에 실패했습니다."),
	SAVE_ACCOUNTS_LIST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "계좌 목록 저장에 실패했습니다."),
	PASSWORD_ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 암호화 실패"),
	ACCESS_TOKEN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AccessToken 발급 실패"),
	CARD_1_PROCESS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카드1 처리 실패"),
	CARD_2_PROCESS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카드2 처리 실패"),
	LACK_BALANCE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "계좌 잔액이 부족합니다."),
	QR_CODE_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "QR 코드 생성에 실패했습니다.");

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
