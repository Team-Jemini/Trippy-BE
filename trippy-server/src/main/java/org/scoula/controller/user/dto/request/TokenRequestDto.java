package org.scoula.controller.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "액세스 토큰 리프레시 DTO")
public record TokenRequestDto(
	@ApiModelProperty(value = "엑세스 토큰", example = "eyJ0e~")
	String accessToken,
	@ApiModelProperty(value = "리프레시 토큰", example = "eyJ0e~")
	String refreshToken) {
}