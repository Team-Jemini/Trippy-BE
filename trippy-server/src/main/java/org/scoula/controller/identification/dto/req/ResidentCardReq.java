package org.scoula.controller.identification.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "주민등록증 추가 DTO")
public record ResidentCardReq(
	@ApiModelProperty(value = "주민등록증 사진", example = "a.png")
	String imgUrl,
	@ApiModelProperty(value = "주민등록증 이름", example = "강병현")
	String name,
	@ApiModelProperty(value = "주민등록 번호", example = "110101-1234567")
	String identity,
	@ApiModelProperty(value = "주소", example = "서울특별시 강남구 역삼동 123-4")
	String address,
	@ApiModelProperty(value = "발급일자", example = "2015-03-10")
	String resIssueDate
) {
}
