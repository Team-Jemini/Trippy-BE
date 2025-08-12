package org.scoula.controller.account.dto.request;

import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.DeletedStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "개인계좌 등록 DTO")
public record AccountRequestDTO(
        // Long userId, -> 애는 파람으로도 받아요
		@ApiModelProperty(value = "계좌 번호", example = "3333-02-123456")
        String accountId,
		@ApiModelProperty(value = "계좌 이름", example = "강병현의 계좌")
        String accountName,
        AccountType accountType,
		@ApiModelProperty(value = "소유자 userId", example = "1")
        Long ownerId,
		@ApiModelProperty(value = "잔액", example = "10000")
        Long balance,
		@ApiModelProperty(value = "통화코드", example = "KOR")
        String accountCurrency,
		@ApiModelProperty(value = "삭제 여부(Y,N)", example = "N")
        DeletedStatus isDeleted
) {
}
