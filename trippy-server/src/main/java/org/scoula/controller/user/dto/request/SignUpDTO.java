package org.scoula.controller.user.dto.request;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "회원가입 요청 DTO")
public record SignUpDTO(
	@ApiModelProperty(value = "이름", example = "안현주")
	@NotBlank(message = "이름은 필수입니다.")
	String name,
	@ApiModelProperty(value = "주민등록번호 앞 6자리-성별코드", example = "001125-4")
	@Pattern(regexp = "^[0-9]{6}-[1-4]$", message = "주민등록번호 형식이 올바르지 않습니다.")
	String residentNum,
	@ApiModelProperty(value = "휴대폰 번호", example = "010-2388-3936")
	@NotBlank(message = "전화번호는 필수입니다.")
	@Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
	String phone,
	@ApiModelProperty(value = "비밀번호", example = "123456")
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min = 6, max = 6, message = "비밀번호는 정확히 6자리여야 합니다.")
	String password
) {
}
