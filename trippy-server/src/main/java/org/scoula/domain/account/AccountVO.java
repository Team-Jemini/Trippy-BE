package org.scoula.domain.account;

import org.scoula.controller.account.dto.request.AccountRequestDTO;
import org.scoula.domain.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AccountVO extends BaseTime {
	private Long userId;
	private String accountId;
	private String accountName;
	private AccountType accountType;
	private Long ownerId;
	private Long balance;
	private String accountCurrency;
	private DeletedStatus isDeleted;

	public static AccountVO from(AccountRequestDTO request, Long userId) {
		return new AccountVO(
				userId,
				request.accountId(),
				request.accountName(),
				request.accountType(),
				userId,
				request.balance(),
				request.accountCurrency(),
				request.isDeleted()
		);
	}

	public static AccountVO fromCodefResponse(Map<String, Object> codefAccount, Long userId) {
		String balanceStr = (String) codefAccount.get("resAccountBalance");
		Long balance = 0L;
		if (balanceStr != null && !balanceStr.isEmpty()) {
			balance = Long.parseLong(balanceStr);
		}

		return new AccountVO(
				userId,
				(String) codefAccount.get("resAccountDisplay"),
				(String) codefAccount.get("resAccountName"),
				AccountType.person,
				userId,
				balance,
				(String) codefAccount.get("resAccountCurrency"),
				DeletedStatus.N
		);
	}
}