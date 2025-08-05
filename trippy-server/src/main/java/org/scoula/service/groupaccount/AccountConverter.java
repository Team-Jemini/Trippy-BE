package org.scoula.service.groupaccount;

import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.domain.account.AccountForeign;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.account.member.AccountMemberVO;
import org.scoula.domain.account.member.Role;

public class AccountConverter {

	public static AccountVO toAccountVO(String accountId, Long userId, AccountType accountType,
		GroupAccountCreateRequestDTO request) {
		return AccountVO.builder()
			.accountId(accountId)
			.userId(userId)
			.accountName(request.accountName())
			.accountType(accountType)
			.ownerId(userId)
			.balance(0L)
			.accountForeign(AccountForeign.kor)
			.isDeleted(DeletedStatus.N)
			.build();
	}

	public static AccountMemberVO toAccountMemberVO(String accountId, Long userId, String mainAccountId, Role role) {
		return AccountMemberVO.builder()
			.accountId(accountId)
			.userId(userId)
			.role(role)
			.mainAccountId(mainAccountId)
			.build();
	}
}