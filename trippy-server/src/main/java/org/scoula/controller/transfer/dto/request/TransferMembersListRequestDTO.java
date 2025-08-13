package org.scoula.controller.transfer.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "정산 멤버 DTO")
public record TransferMembersListRequestDTO(
	@ApiModelProperty(value = "메인 계좌Id", example = "3333-02-123458")
	String mainAccountId,
	@ApiModelProperty(value = "유저Id", example = "1")
	Long userId,
	@ApiModelProperty(value = "유저이름", example = "홍길동")
	String userName) {
}
