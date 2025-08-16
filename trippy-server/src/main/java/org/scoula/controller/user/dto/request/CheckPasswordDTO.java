package org.scoula.controller.user.dto.request;

import javax.validation.constraints.Size;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "비밀번호 확인 DTO")
public record CheckPasswordDTO(
	@ApiModelProperty(value = "비밀번호", example = "123456")
	// @NotBlank(message = "비밀번호는 필수입니다.")
	// @Size(min = 6, max = 6, message = "비밀번호는 정확히 6자리여야 합니다.")
	String password
) {
}
