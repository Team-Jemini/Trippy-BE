package org.scoula.controller.groupAccount.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "모임 계좌 참여 DTO")
public record GroupAccountJoinRequestDTO(
	@ApiModelProperty(value = "초대 토큰")
	String token,
	@ApiModelProperty(value = "모임계좌 ID", example = "3333-02-123457")
	String mainAccountId
) {
}
