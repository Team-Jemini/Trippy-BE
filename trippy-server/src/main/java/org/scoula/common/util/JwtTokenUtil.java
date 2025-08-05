package org.scoula.common.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.controller.invite.dto.response.AcceptInviteResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JwtTokenUtil {

	private final Key secretKey;
	private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24; // 24시간

	public JwtTokenUtil(@Value("${invite.SECRET-key}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String createInviteToken(Long userId, String accountId, String accountName) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

		return Jwts.builder()
			.setSubject("invite")
			.claim("accountId", accountId)
			.claim("userId", userId)
			.claim("accountName", accountName)
			.claim("expiryDate", Long.valueOf(expiryDate.getTime()))
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public AcceptInviteResponseDTO parseInviteToken(String token) {
		try {
			Jws<Claims> climsJws = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);

			Claims claims = climsJws.getBody();

			String accountId = claims.get("accountId", String.class);
			String accountName = claims.get("accountName", String.class);

			Number userIdNumber = claims.get("userId", Number.class);
			Long userId = userIdNumber.longValue();

			Number expiryDateNumber = claims.get("expiryDate", Number.class);
			if (expiryDateNumber == null) {
				throw new RuntimeException(ErrorCode.INVALID_INVITE_TOKEN.getMessage());
			}
			Long expiryDateMillis = expiryDateNumber.longValue();
			LocalDateTime expiryDate = LocalDateTime.ofEpochSecond(expiryDateMillis / 1000, 0, ZoneOffset.UTC);

			return new AcceptInviteResponseDTO(accountId, accountName, userId, expiryDate);

		} catch (ExpiredJwtException e) {
			throw new RuntimeException(ErrorCode.EXPIRED_INVITE_TOKEN.getMessage());
		} catch (SignatureException e) {
			throw new SignatureException(ErrorCode.INVALID_INVITE_TOKEN.getMessage());
		} catch (MalformedJwtException | UnsupportedJwtException e) {
			throw new MalformedJwtException(ErrorCode.INVALID_INVITE_TOKEN.getMessage());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(ErrorCode.INVALID_REQUEST_PARAMETER.getMessage());
		} catch (JwtException e) {
			throw new JwtException(ErrorCode.INVALID_INVITE_TOKEN.getMessage());
		}
	}
}
