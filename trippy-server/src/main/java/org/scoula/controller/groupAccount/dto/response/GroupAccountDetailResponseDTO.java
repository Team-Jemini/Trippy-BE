package org.scoula.controller.groupAccount.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.account.member.Role;

public record GroupAccountDetailResponseDTO(
	Long userId,
	String accountId,
	String accountName,
	AccountType accountType,
	Long ownerId,
	Long balance,
	String accountCurrency,
	DeletedStatus isDeleted,
	Role role,
	LocalDateTime createdAt,
	List<DailyAccountTransactionDTO> transactions
) {
}
