package org.scoula.service.groupaccount;

import java.util.List;

import org.scoula.controller.groupAccount.dto.response.GroupAccountMemberResponseDTO;
import org.scoula.mapper.account.member.AccountMemberMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {
	private final AccountMemberMapper Membermapper;

	public List<GroupAccountMemberResponseDTO> getGroupAccountMembers(String accountId) {
		return Membermapper.findAllGroupMembers(accountId).stream()
			.map(GroupAccountMemberResponseDTO::from)
			.toList();
	}
}
