package org.scoula.controller.groupAccount.dto.response;

import java.util.List;

import org.scoula.domain.account.AccountForeign;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.account.member.Role;

public record GroupAccountDetailResponseDTO(
	Long userId,
	String accountName,
	String accountId,
	AccountType accountType,
	Long ownerId,
	Long balance,
	AccountForeign accountForeign,
	DeletedStatus isDeleted,
	Role role,
	List<AccountTransactionResponseDTO> accountTransactions
) {
}
