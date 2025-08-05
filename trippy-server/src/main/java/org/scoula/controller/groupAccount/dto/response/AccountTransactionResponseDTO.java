package org.scoula.controller.groupAccount.dto.response;

import org.scoula.domain.transaction.TransactionCategory;
import org.scoula.domain.transaction.TransactionType;
import org.springframework.transaction.TransactionStatus;

public record AccountTransactionResponseDTO(
	String accountId,
	Long userId,
	Long transactionId,
	TransactionType transactionType,
	Long amount,
	String title,
	TransactionCategory category,
	String latitude,
	String longitude,
	Long balanceAfter,
	TransactionStatus status,
	String currencyCode
) {
}
