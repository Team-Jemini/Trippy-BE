package org.scoula.domain.account.group;

import org.scoula.domain.BaseTime;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.account.member.Role;

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
public class GroupAccountVO extends BaseTime {
	private Long userId;
	private String accountId;
	private String accountName;
	private AccountType accountType;
	private Long ownerId;
	private Long balance;
	private String accountCurrency;
	private DeletedStatus isDeleted;
	private Role role;
}
