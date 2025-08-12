package org.scoula.common.exception;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.exception.model.ServerErrorException;
import org.scoula.common.exception.model.TrippyException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	@Autowired
	private DiscordNotificationService discordNotificationService;

	// ServerErrorException만 500 에러로 처리 (Discord 500 채널)
	@ExceptionHandler(ServerErrorException.class)
	public ResponseEntity<ErrorResponse> handleServerErrorException(ServerErrorException ex, HttpServletRequest request) {
		log.error("=== 500 ServerErrorException 발생 ===");
		log.error("에러 타입: {}", ex.getClass().getSimpleName());
		log.error("에러 메시지: {}", ex.getMessage());
		log.error("스택트레이스: ", ex);

		// 요청 정보 수집
		String requestInfo = String.format("%s %s",
			request.getMethod(),
			request.getRequestURI());

		// 스택 트레이스를 문자열로 변환
		java.io.StringWriter sw = new java.io.StringWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(sw);
		ex.printStackTrace(pw);
		String stackTrace = sw.toString();

		// 500 에러 Discord 알림 발송
		send500Notification(ex.getMessage(), stackTrace, requestInfo);

		return ResponseEntity
			.status(ex.getErrorCode().getHttpStatus())
			.body(ErrorResponse.error(ex.getErrorCode()));
	}

	// 나머지 TrippyException들은 400대 에러로 처리 (Discord 400 채널)
	@ExceptionHandler(TrippyException.class)
	public ResponseEntity<ErrorResponse> handleTrippyException(TrippyException ex, HttpServletRequest request) {
		log.warn("=== 400대 TrippyException 발생 ===");
		log.warn("에러 타입: {}", ex.getClass().getSimpleName());
		log.warn("에러 메시지: {}", ex.getMessage());
		log.warn("에러 코드: {}", ex.getErrorCode());

		// 요청 정보 수집
		String requestInfo = String.format("%s %s",
			request.getMethod(),
			request.getRequestURI());

		// 클라이언트 정보 수집
		String clientInfo = String.format("IP: %s, User-Agent: %s",
			getClientIP(request),
			request.getHeader("User-Agent"));

		// 400대 에러 Discord 알림 발송
		send400Notification(ex.getClass().getSimpleName() + ": " + ex.getMessage(), requestInfo, clientInfo);

		return ResponseEntity
			.status(ex.getErrorCode().getHttpStatus())
			.body(ErrorResponse.error(ex.getErrorCode()));
	}

	// 404 에러 처리 (Discord 400 채널)
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {

		//아래와 같은 경우는 디코 로깅 안되도록
		String uri = request.getRequestURI();
		if ("/csrf".equals(uri) || "/".equals(uri)) {
			return ResponseEntity.notFound().build();
		}

		// 상세 로깅
		log.warn("=== 4XX 에러 상세 정보 ===");
		log.warn("요청 URI: {}", request.getRequestURI());
		log.warn("요청 Method: {}", request.getMethod());
		log.warn("User-Agent: {}", request.getHeader("User-Agent"));
		log.warn("Referer: {}", request.getHeader("Referer"));
		log.warn("Remote IP: {}", getClientIP(request));
		log.warn("Query String: {}", request.getQueryString());
		log.warn("================================");

		// 400대 에러 Discord 알림
		String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());
		String clientInfo = String.format("IP: %s, User-Agent: %s",
			getClientIP(request),
			request.getHeader("User-Agent"));

		send400Notification("404 Not Found: " + request.getRequestURI(), requestInfo, clientInfo);

		return ResponseEntity.notFound().build();
	}

	// Spring에서 자동으로 던지는 400대 에러들 처리
	@ExceptionHandler({
		HttpMessageNotReadableException.class,           // JSON 파싱 실패
		MethodArgumentNotValidException.class,           // @Valid 검증 실패
		MissingServletRequestParameterException.class,   // 필수 파라미터 누락
		HttpRequestMethodNotSupportedException.class,    // 잘못된 HTTP 메서드
		HttpMediaTypeNotSupportedException.class,        // 잘못된 Content-Type
		TypeMismatchException.class                      // 타입 변환 실패
	})
	public ResponseEntity<ErrorResponse> handleSpringBadRequest(Exception ex, HttpServletRequest request) {
		log.warn("=== Spring 400대 에러 발생 ===");
		log.warn("에러 타입: {}", ex.getClass().getSimpleName());
		log.warn("에러 메시지: {}", ex.getMessage());

		String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());
		String clientInfo = String.format("IP: %s, User-Agent: %s",
			getClientIP(request),
			request.getHeader("User-Agent"));

		send400Notification(ex.getClass().getSimpleName() + ": " + ex.getMessage(), requestInfo, clientInfo);

		return ResponseEntity
			.badRequest()
			.body(ErrorResponse.badRequestError("잘못된 요청입니다."));
	}

	// 일반 예외 처리 (500 에러 - Discord 500 채널)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
		log.error("=== 500 일반 예외 발생 ===");
		log.error("에러 타입: {}", ex.getClass().getSimpleName());
		log.error("에러 메시지: {}", ex.getMessage());
		log.error("스택트레이스: ", ex);

		// 요청 정보 수집
		String requestInfo = String.format("%s %s",
			request.getMethod(),
			request.getRequestURI());

		// 스택 트레이스를 문자열로 변환
		java.io.StringWriter sw = new java.io.StringWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(sw);
		ex.printStackTrace(pw);
		String stackTrace = sw.toString();

		// 500 에러 Discord 알림 발송
		send500Notification(ex.getMessage(), stackTrace, requestInfo);

		return ResponseEntity
			.internalServerError()
			.body(ErrorResponse.badRequestError("서버 내부 오류가 발생했습니다."));
	}

	// 500 에러 Discord 알림 전송
	private void send500Notification(String errorMessage, String stackTrace, String requestInfo) {
		new Thread(() -> {
			try {
				discordNotificationService.sendErrorNotification(errorMessage, stackTrace, requestInfo);
			} catch (Exception discordEx) {
				log.error("Discord 500 알림 전송 중 에러 발생: {}", discordEx.getMessage());
			}
		}).start();
	}

	// 400대 에러 Discord 알림 전송
	private void send400Notification(String errorMessage, String requestInfo, String clientInfo) {
		new Thread(() -> {
			try {
				discordNotificationService.send4xxNotification(errorMessage, requestInfo, clientInfo);
			} catch (Exception discordEx) {
				log.error("Discord 400 알림 전송 중 에러 발생: {}", discordEx.getMessage());
			}
		}).start();
	}

	// 클라이언트 IP 추출
	private String getClientIP(HttpServletRequest request) {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}
}
