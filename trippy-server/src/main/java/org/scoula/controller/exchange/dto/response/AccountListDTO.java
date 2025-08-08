package org.scoula.controller.exchange.dto.response;

import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.exchange.AccountListVO;

public record AccountListDTO(
	String accountId,
	String accountName,
	Long balance,
	String accountForeign,
	DeletedStatus isDeleted
) {
	public static AccountListDTO from(AccountListVO vo) {
		return new AccountListDTO(
			vo.getAccountId(),
			vo.getAccountName(),
			vo.getBalance(),
			vo.getAccountCurrency(),
			vo.getIsDeleted()
		);
	}
}
