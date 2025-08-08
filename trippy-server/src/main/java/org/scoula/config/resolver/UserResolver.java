package org.scoula.config.resolver;

import static org.scoula.common.exception.enums.ErrorCode.*;

import javax.servlet.http.HttpServletRequest;

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
			throw new BadRequestException(TOKEN_NOT_CONTAINED_EXCEPTION);
		}

		final String rawToken = token.substring("Bearer ".length());

		try {
			jwtService.verifyToken(rawToken);

			if (!jwtService.isAccessToken(rawToken)) {
				throw new BadRequestException(INVALID_TOKEN_TYPE_EXCEPTION);
			}

			final String userId = jwtService.getUserIdInToken(rawToken);
			return Long.parseLong(userId);
		} catch (ExpiredJwtException e) {
			throw new UnAuthorizedException(TOKEN_TIME_EXPIRED_EXCEPTION);
		} catch (Exception e) {
			throw new BadRequestException(INVALID_TOKEN_EXCEPTION);
		}
	}
}

