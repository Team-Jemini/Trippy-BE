package org.scoula.controller.groupAccount.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "모임계좌 생성 DTO")
public record GroupAccountCreateRequestDTO(
	@ApiModelProperty(value = "계좌 이름")
	String accountName,
	@ApiModelProperty(value = "계좌계약서 받을 이메일")
	String email,
	@ApiModelProperty(value = "정산받을 계좌")
	String mainAccountId
) {
}