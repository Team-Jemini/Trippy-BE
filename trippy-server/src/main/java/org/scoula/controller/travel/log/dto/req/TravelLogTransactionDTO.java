package org.scoula.controller.travel.log.dto.req;

import java.time.LocalDateTime;

import org.scoula.domain.transaction.TransactionCategory;
import org.scoula.domain.transaction.TransactionVO;

public record TravelLogTransactionDTO(
	Long transactionId,
	TransactionCategory category,
	Long amount,
	LocalDateTime createdAt,
	String title
) {
	public static TravelLogTransactionDTO from(TransactionVO transaction) {
		return new TravelLogTransactionDTO(
			transaction.getTransactionId(),
			transaction.getCategory(),
			transaction.getAmount(),
			transaction.getCreatedAt(),
			transaction.getTitle()
		);
	}
}
