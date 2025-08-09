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

		if (token == null || token.isBlank() || !token.startsWith("Bearer ")) {
			throw new BadRequestException(ErrorCode.TOKEN_NOT_CONTAINED_EXCEPTION);
		}

		final String rawToken = token.substring("Bearer ".length());

		// (1) 블랙리스트 체크: 과거 AccessToken 차단
		if (jwtService.isBlacklisted(rawToken)) {
			throw new UnAuthorizedException(ErrorCode.BLACKLISTED_TOKEN_EXCEPTION);
		}

		try {
			jwtService.verifyToken(rawToken);

			// (2) 이 토큰은 AccessToken이어야 함(Refresh 토큰 차단)
			if (!jwtService.isAccessToken(rawToken)) {
				throw new BadRequestException(ErrorCode.INVALID_TOKEN_TYPE_EXCEPTION);
			}

			final String userId = jwtService.getUserIdInToken(rawToken);
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

