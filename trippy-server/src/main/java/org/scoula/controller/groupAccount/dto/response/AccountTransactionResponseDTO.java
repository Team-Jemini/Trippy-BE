package org.scoula.controller.groupAccount.dto.response;

import java.time.LocalDateTime;

public record AccountTransactionResponseDTO(
	Long transactionId,
	String transactionType,
	Long amount,
	Long balanceAfter,
	String title,
	String category,
	String status,
	String currencyCode,
	LocalDateTime createdAt
) {
}
