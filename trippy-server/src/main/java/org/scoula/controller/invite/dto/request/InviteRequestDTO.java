package org.scoula.controller.invite.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "초대링크 DTO")
public record InviteRequestDTO(
	@ApiModelProperty(value = "계좌 번호")
	String accountId,
	@ApiModelProperty(value = "계좌 이름")
	String accountName
) {
}
