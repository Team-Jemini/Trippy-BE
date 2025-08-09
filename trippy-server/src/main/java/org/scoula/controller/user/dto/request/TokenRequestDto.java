package org.scoula.controller.user.dto.request;

public record TokenRequestDto(
	String accessToken,
	String refreshToken) {
}