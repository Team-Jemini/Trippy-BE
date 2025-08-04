package org.scoula.common.exception;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.exception.model.TrippyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TrippyException.class)
	public ResponseEntity<ErrorResponse> handleTrippyException(TrippyException ex) {
		return ResponseEntity
			.status(ex.getErrorCode().getHttpStatus())
			.body(ErrorResponse.error(ex.getErrorCode()));
	}

	// Optional: 그 외 예상치 못한 예외 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		return ResponseEntity
			.internalServerError()
			.body(ErrorResponse.badRequestError("서버 내부 오류가 발생했습니다."));
	}
}