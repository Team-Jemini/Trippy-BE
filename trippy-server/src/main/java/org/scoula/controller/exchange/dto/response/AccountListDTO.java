package org.scoula.controller.exchange.dto.response;

import org.scoula.domain.account.DeletedStatus;

public record AccountListDTO(
	String accountId,
	String accountName,
	Long balance,
	String accountForeign,
	DeletedStatus isDeleted) {
	public static AccountListDTO from(
		String accountId,
		String accountName,
		Long balance,
		String accountForeign,
		DeletedStatus isDeleted
	) {
		return new AccountListDTO(accountId, accountName, balance, accountForeign, isDeleted);
	}
}
