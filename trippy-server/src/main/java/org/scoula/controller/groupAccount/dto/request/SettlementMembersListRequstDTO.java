package org.scoula.controller.groupAccount.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "정산 멤버 DTO")
public record SettlementMembersListRequstDTO(
	@ApiModelProperty(value = "메인 계좌Id", example = "3333-02-123458")
	String mainAccountId,
	@ApiModelProperty(value = "메인 계좌Id", example = "1")
	Long userId
) {
}
