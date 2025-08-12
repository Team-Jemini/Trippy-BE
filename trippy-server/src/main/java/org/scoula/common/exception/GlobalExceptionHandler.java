package org.scoula.common.exception;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.exception.model.TrippyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	@ExceptionHandler(TrippyException.class)
	public ResponseEntity<ErrorResponse> handleTrippyException(TrippyException ex) {
		// 로그 추가
		log.error("TrippyException 발생: {}", ex.getMessage(), ex);

		return ResponseEntity
			.status(ex.getErrorCode().getHttpStatus())
			.body(ErrorResponse.error(ex.getErrorCode()));
	}

	// 일반 예외 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		log.error("=== 500서버 에러 발생 ===");
		log.error("에러 타입: {}", ex.getClass().getSimpleName());
		log.error("에러 메시지: {}", ex.getMessage());
		log.error("스택트레이스: ", ex);

		return ResponseEntity
			.internalServerError()
			.body(ErrorResponse.badRequestError("서버 내부 오류가 발생했습니다."));
	}
}
