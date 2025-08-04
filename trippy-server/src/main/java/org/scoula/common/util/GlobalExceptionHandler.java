package org.scoula.common.util;

import org.scoula.common.exception.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
		String message = e.getMessage();

		ErrorCode errorCode = findErrorCodeByMessage(message);

		if (errorCode == null) {
			errorCode = ErrorCode.INTERNAL_SERVER_EXCEPTION;
		}

		ErrorResponse response = new ErrorResponse(errorCode.name(), errorCode.getMessage());
		return new ResponseEntity<>(response, errorCode.getHttpStatus());
	}

	private ErrorCode findErrorCodeByMessage(String message) {
		for (ErrorCode code : ErrorCode.values()) {
			if (code.getMessage().equals(message)) {
				return code;
			}
		}
		return null;
	}

	@Getter
	@AllArgsConstructor
	public static class ErrorResponse {
		private String code;
		private String message;
	}
}