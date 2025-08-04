package org.scoula.common.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

}
