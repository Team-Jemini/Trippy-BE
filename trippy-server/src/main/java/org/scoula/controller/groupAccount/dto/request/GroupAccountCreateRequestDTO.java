package org.scoula.controller.groupAccount.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "모임 계좌 생성 DTO")
public record GroupAccountCreateRequestDTO(
	@ApiModelProperty(value = "모임계좌 이름", example = "7트리피의 모임계좌")
	String accountName,
	@ApiModelProperty(value = "모임장의 이메일", example = "trippy@gmail.com")
	String email,
	@ApiModelProperty(value = "메인 계좌Id", example = "3333-02-123458")
	String mainAccountId
) {
}