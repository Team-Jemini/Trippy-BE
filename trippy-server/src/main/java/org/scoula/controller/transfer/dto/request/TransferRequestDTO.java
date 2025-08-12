package org.scoula.controller.transfer.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "송금 요청 DTO")
public record TransferRequestDTO(
	@ApiModelProperty(value = "(보내는 이)계좌 번호", example = "3333-02-123456")
    String fromAccountId,
	@ApiModelProperty(value = "(받는 이)계좌 번호", example = "3333-02-123456")
	String toAccountId,
	@ApiModelProperty(value = "송금액", example = "150000")
    Long amount,
	@ApiModelProperty(value = "송금화폐 (ex. KOR)", example = "KOR")
    String currencyCode,
	@ApiModelProperty(value = "송금 제목", example = "현주야 받아라")
    String title
) {}
