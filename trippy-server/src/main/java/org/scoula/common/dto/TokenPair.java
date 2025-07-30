package org.scoula.common.dto;

public record TokenPair(
	String accessToken, String refreshToken
) {
}