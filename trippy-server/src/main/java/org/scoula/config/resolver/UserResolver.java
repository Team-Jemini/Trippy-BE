package org.scoula.config.resolver;

import javax.servlet.http.HttpServletRequest;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.BadRequestException;
import org.scoula.common.exception.model.UnAuthorizedException;
import org.scoula.config.jwt.JwtService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserResolver implements HandlerMethodArgumentResolver {
	private final JwtService jwtService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(UserId.class) && Long.class.equals(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) {

		final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		final String token = request.getHeader("Authorization");

		log.info("=== UserResolver 시작 ===");
		log.info("Request URI: {}", request.getRequestURI());
		log.info("Authorization Header: {}", token);

		if (token == null || token.isBlank() || !token.startsWith("Bearer ")) {
			throw new BadRequestException(ErrorCode.TOKEN_NOT_CONTAINED_EXCEPTION);
		}

		final String rawToken = token.substring("Bearer ".length());
		log.info("Raw Token (첫 20자): {}", rawToken.substring(0, Math.min(20, rawToken.length())));

		try {
			log.info("2. 토큰 검증 시작");

			jwtService.verifyToken(rawToken);
			log.info("2. 토큰 검증 완료");


			// (2) 이 토큰은 AccessToken이어야 함(Refresh 토큰 차단)
			if (!jwtService.isAccessToken(rawToken)) {
				throw new BadRequestException(ErrorCode.INVALID_TOKEN_TYPE_EXCEPTION);
			}

			log.info("4. UserId 추출 시작");

			final String userId = jwtService.getUserIdInToken(rawToken);
			log.info("4. 추출된 UserId: {}", userId);
			return Long.parseLong(userId);

		} catch (ExpiredJwtException e) {
			throw new UnAuthorizedException(ErrorCode.TOKEN_TIME_EXPIRED_EXCEPTION);
		} catch (BadRequestException | UnAuthorizedException e) {
			throw e;
		} catch (Exception e) {
			throw new BadRequestException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}
	}
}

