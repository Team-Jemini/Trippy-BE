package org.scoula.common.dto;

import org.scoula.common.exception.enums.SuccessCode;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
// @AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessNonDataResponse {
	private final int code;
	private final String message;

	public SuccessNonDataResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public static SuccessNonDataResponse success(SuccessCode successCode) {
		return new SuccessNonDataResponse(successCode.getHttpStatus().value(), successCode.getMessage());
	}
}