package org.scoula.controller.invite.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "초대링크 DTO")
public record InviteResponseDTO(

	@ApiModelProperty(value = "초대링크")
	String inviteTokenURL
) {
}
