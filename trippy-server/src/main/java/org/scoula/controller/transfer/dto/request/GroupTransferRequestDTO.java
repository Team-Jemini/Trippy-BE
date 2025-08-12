package org.scoula.controller.transfer.dto.request;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "모임계좌 송금 요청 DTO")
public record GroupTransferRequestDTO(
	@ApiModelProperty(value = "(보내는 이)계좌 번호", example = "3333-02-123456")
	String fromAccountId,
	@ApiModelProperty(value = "송금액", example = "150000")
	Long amount,
	@ApiModelProperty(value = "송금화폐 (ex. KOR)", example = "KOR")
	String currencyCode,
	@ApiModelProperty(value = "받는이 목록", example = "받는이 계좌 번호, 유저Id, 유저이름")
	List<TransferMembersListRequestDTO> memberList
) {
}
