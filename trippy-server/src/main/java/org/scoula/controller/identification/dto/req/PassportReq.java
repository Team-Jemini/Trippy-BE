package org.scoula.controller.identification.dto.req;

import org.scoula.domain.user.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "여권 정보 DTO")
public record PassportReq(
	@ApiModelProperty(value = "여권 번호", example = "M11223355")
	String passportNumber,
	@ApiModelProperty(value = "한국 이름", example = "강병현")
	String nameKr,
	@ApiModelProperty(value = "영어 이름", example = "BYUNGHYWON KANG")
	String nameEn,
	@ApiModelProperty(value = "생년월일", example = "2000-02-17")
	LocalDate birthDate,
	@ApiModelProperty(value = "성별(M or F)", example = "M")
	Gender gender,
	@ApiModelProperty(value = "국가코드", example = "KOR")
	String countryCode,
	@ApiModelProperty(value = "만료일자", example = "2029-02-25")
	LocalDate expireDate
) {

}
