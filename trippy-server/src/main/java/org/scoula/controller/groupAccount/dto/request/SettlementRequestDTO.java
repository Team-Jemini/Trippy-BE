package org.scoula.controller.groupAccount.dto.request;

public record SettlementRequestDTO(
	String accountId,
	String accountName,
	String userId,
	Long settlementAmount
) {
}
