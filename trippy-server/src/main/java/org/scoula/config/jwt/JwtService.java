package org.scoula.config.jwt;

import static org.scoula.common.exception.enums.ErrorCode.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.scoula.common.dto.TokenPair;
import org.scoula.common.exception.model.UnAuthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtService {
	@Value("${jwt.secret}")
	private String jwtSecret;
	private static final String USER_ID = "USER_ID";
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
	public static final int MINUTE_IN_MILLISECONDS = 60 * 1000;
	public static final long DAYS_IN_MILLISECONDS = 24 * 60 * 60 * 1000L;
	// public static final int ACCESS_TOKEN_EXPIRATION_MINUTE = 10;
	// public static final int REFRESH_TOKEN_EXPIRATION_DAYS = 14;
	public static final int ACCESS_TOKEN_EXPIRATION_DAYS = 30; //30 days
	public static final int REFRESH_TOKEN_EXPIRATION_DAYS = 60; //60days
	private final RedisTemplate<String, String> redisTemplate;

	@PostConstruct
	protected void init() {
		jwtSecret = Base64.getEncoder()
			.encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	public String createAccessToken(final String userId) {
		final Claims claims = getAccessTokenClaims();

		claims.put(USER_ID, userId);
		return createToken(claims);
	}

	public String createRefreshToken(final String userId) {
		final Claims claims = getRefreshTokenClaims();

		claims.put(USER_ID, userId);
		return createToken(claims);
	}

	public boolean isAccessToken(final String token) {
		try {
			final Claims claims = getBody(token);
			return ACCESS_TOKEN.equals(claims.getSubject());
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isRefreshToken(final String token) {
		try {
			final Claims claims = getBody(token);
			return REFRESH_TOKEN.equals(claims.getSubject());
		} catch (Exception e) {
			return false;
		}
	}

	public void verifyToken(final String token) {
		try {
			getBody(token);
		} catch (ExpiredJwtException e) {
			throw new UnAuthorizedException(TOKEN_TIME_EXPIRED_EXCEPTION);
		} catch (Exception e) {
			throw new UnAuthorizedException(INVALID_TOKEN_EXCEPTION);
		}
	}

	public String getUserIdInToken(final String token) {
		final Claims claims = getBody(token);
		return (String) claims.get(USER_ID);
	}

	public TokenPair generateTokenPair(final String userId) {
		deleteRefreshToken(userId);
		final String accessToken = createAccessToken(userId);
		final String refreshToken = createRefreshToken(userId);
		saveRefreshToken(userId, refreshToken);
		return new TokenPair(accessToken, refreshToken);
	}

	public boolean compareRefreshToken(final String userId, final String refreshToken) {
		final String storedRefreshToken = redisTemplate.opsForValue().get(userId);
		if (storedRefreshToken == null) return false;
		return storedRefreshToken.equals(refreshToken);
	}

	public void saveRefreshToken(final String userId, final String refreshToken) {
		try {
			redisTemplate.opsForValue().set(userId, refreshToken, REFRESH_TOKEN_EXPIRATION_DAYS, TimeUnit.DAYS);
		} catch (Exception e) {
			log.error("Redis 저장 실패 - userId: {}, message: {}", userId, e.getMessage(), e);
		}
	}

	private String createToken(final Claims claims) {
		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setClaims(claims)
			.signWith(getSigningKey())
			.compact();
	}

	private Claims getRefreshTokenClaims() {
		final Date now = new Date();
		return Jwts.claims()
			.setSubject(REFRESH_TOKEN)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_DAYS * DAYS_IN_MILLISECONDS));
	}

	private Claims getAccessTokenClaims() {
		final Date now = new Date();
		return Jwts.claims()
			.setSubject(ACCESS_TOKEN)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_DAYS * DAYS_IN_MILLISECONDS));
	}

	private Claims getBody(final String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private Key getSigningKey() {
		final byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public void deleteRefreshToken(final String userId) {
		redisTemplate.delete(userId);
	}

	public void blacklistAccessToken(String accessToken) {
		try {
			Claims claims = getBody(accessToken);
			long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();
			if (ttl > 0) {
				redisTemplate.opsForValue().set("BL:" + accessToken, "1", ttl, TimeUnit.MILLISECONDS);
			}
		} catch (ExpiredJwtException e) {
		}
	}

	public boolean isBlacklisted(String accessToken) {
		return Boolean.TRUE.equals(redisTemplate.hasKey("BL:" + accessToken));
	}
}

