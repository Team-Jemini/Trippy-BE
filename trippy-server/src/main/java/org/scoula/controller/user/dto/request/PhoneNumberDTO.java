package org.scoula.controller.user.dto.request;

import javax.validation.constraints.Pattern;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "인증번호 발급 요청 DTO")
public record PhoneNumberDTO(
	@ApiModelProperty(value = "휴대폰 번호", example = "010-1234-5678")
	// @NotBlank(message = "전화번호는 필수입니다.")
	// @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
	String phoneNumber
) {
}
