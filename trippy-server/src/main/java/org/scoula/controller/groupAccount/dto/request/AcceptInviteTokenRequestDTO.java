package org.scoula.controller.groupAccount.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "초대 토큰 DTO")
public record AcceptInviteTokenRequestDTO(
	@ApiModelProperty(value = "초대 토큰")
	String token
) {
}
