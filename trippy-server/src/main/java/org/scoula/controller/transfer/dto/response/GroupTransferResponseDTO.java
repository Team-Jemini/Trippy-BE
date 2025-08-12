package org.scoula.controller.transfer.dto.response;

public record GroupTransferResponseDTO(
	String fromAccountId,
	Long amount,
	Long balance,
	String currencyCode
) {
}
