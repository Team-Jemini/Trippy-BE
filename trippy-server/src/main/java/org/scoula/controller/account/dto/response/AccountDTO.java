package org.scoula.controller.account.dto.response;

import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;

public record AccountDTO(
	Long userId,
	String accountId,
	String accountName,
	AccountType accountType,
	Long ownerId,
	Long balance,
	String accountCurrency,
	DeletedStatus isDeleted
) {
	public static AccountDTO from(AccountVO vo, Long userId) {
		return new AccountDTO(
			userId,
			vo.getAccountId(),
			vo.getAccountName(),
			vo.getAccountType(),
			userId,
			vo.getBalance(),
			vo.getAccountCurrency(),
			vo.getIsDeleted()
		);
	}
}
