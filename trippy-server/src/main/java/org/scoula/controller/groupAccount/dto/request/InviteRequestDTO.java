package org.scoula.controller.groupAccount.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "초대 요청 DTO")
public record InviteRequestDTO(
	@ApiModelProperty(value = "모임계좌Id", example = "3333-02-123457")
	String accountId,
	@ApiModelProperty(value = "모임계좌 이름", example = "7트리피의 모임계좌")
	String accountName
) {
}
