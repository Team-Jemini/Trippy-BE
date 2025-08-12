package org.scoula.controller.groupAccount.dto.request;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "정산 요청 DTO")
public record SettlementRequestDTO(
	@ApiModelProperty(value = "모임 계좌Id", example = "3333-02-123458")
	String accountId,
	@ApiModelProperty(value = "모임계좌 이름", example = "7트리피의 모임계좌")
	String accountName,
	@ApiModelProperty(value = "정산 금액", example = "10000")
	Long amount,
	List<SettlementMembersListRequstDTO> memberList
) {
}
