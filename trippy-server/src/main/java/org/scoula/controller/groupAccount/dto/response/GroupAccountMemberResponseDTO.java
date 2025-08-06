package org.scoula.controller.groupAccount.dto.response;

import org.scoula.domain.account.member.AccountMemberVO;
import org.scoula.domain.account.member.Role;

public record GroupAccountMemberResponseDTO(
	Long userId,
	String userName,
	Role role,
	String mainAccountId
) {
	public static GroupAccountMemberResponseDTO from(AccountMemberVO vo) {
		return new GroupAccountMemberResponseDTO(
			vo.getUserId(),
			vo.getUserName(),
			vo.getRole(),
			vo.getMainAccountId()
		);
	}
}
