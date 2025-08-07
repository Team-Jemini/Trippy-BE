package org.scoula.domain.account;

import org.scoula.domain.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}