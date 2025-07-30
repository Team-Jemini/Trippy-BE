package org.scoula.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

	REFRESH_SUCCESS(HttpStatus.OK, "토큰 갱신 성공입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
