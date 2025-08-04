package org.scoula.controller.groupAccount.dto.response;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "모임계좌 생성 DTO")
public record GroupAccountCreateResponseDTO(
	@ApiModelProperty(value = "생성된 계좌번호")
	String accountId,
	@ApiModelProperty(value = "계좌 이름")
	String accountName,
	@ApiModelProperty(value = "계좌 생성날짜")
	LocalDateTime createdAt
) {
}