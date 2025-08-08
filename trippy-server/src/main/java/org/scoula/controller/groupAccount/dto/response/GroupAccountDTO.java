package org.scoula.controller.groupAccount.dto.response;

import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.account.group.GroupAccountVO;
import org.scoula.domain.account.member.Role;

public record GroupAccountDTO(
	Long userId,
	String accountId,
	String accountName,
	AccountType accountType,
	Long ownerId,
	Long balance,
	String accountCurrency,
	DeletedStatus isDeleted,
	Role role
) {
	public static GroupAccountDTO from(GroupAccountVO vo, Long userId) {
		return new GroupAccountDTO(
			userId,
			vo.getAccountId(),
			vo.getAccountName(),
			vo.getAccountType(),
			userId,
			vo.getBalance(),
			vo.getAccountCurrency(),
			vo.getIsDeleted(),
			vo.getRole()
		);
	}
}
