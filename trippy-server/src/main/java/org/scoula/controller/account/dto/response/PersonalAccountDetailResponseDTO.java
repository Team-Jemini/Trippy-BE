package org.scoula.controller.account.dto.response;

import java.util.List;

import org.scoula.controller.groupAccount.dto.response.AccountTransactionResponseDTO;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.DeletedStatus;

public record PersonalAccountDetailResponseDTO(
	Long userId,
	String accountId,
	String accountName,
	AccountType accountType,
	Long ownerId,
	Long balance,
	String accountCurrency,
	DeletedStatus isDeleted,
	List<AccountTransactionResponseDTO> transactions
) {
}
