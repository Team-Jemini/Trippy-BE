package org.scoula.service.groupaccount;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.TrippyException;
import org.scoula.common.util.JwtTokenUtil;
import org.scoula.controller.groupAccount.dto.request.GroupAccountJoinRequestDTO;
import org.scoula.controller.groupAccount.dto.response.AcceptInviteResponseDTO;
import org.scoula.controller.groupAccount.dto.response.InviteResponseDTO;
import org.scoula.mapper.account.group.GroupAccountMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class InviteService {

	private final JwtTokenUtil jwtTokenUtil;
	private final String BASE_URL = "http://localhost:5173/?token=";
	private final GroupAccountMapper mapper;

	public InviteResponseDTO createInviteTokenURL(Long userId, String accountId, String accountName) {
		String userName = mapper.selectUserName(userId);
		return new InviteResponseDTO(
			BASE_URL + jwtTokenUtil.createInviteToken(userId, userName, accountId, accountName));
	}

	public AcceptInviteResponseDTO parseInviteToken(Long userId, String token) {

		AcceptInviteResponseDTO response = jwtTokenUtil.parseInviteToken(token);
		int count = mapper.searchJoinUser(userId, response.accountId());
		if (count > 0) {
			throw new TrippyException(ErrorCode.ALREADY_INVITED);
		}
		return response;
	}

	public void joinGroupAccount(Long userId, GroupAccountJoinRequestDTO request) {
		mapper.groupAccountJoin(
			AccountConverter.toAccountMemberVO(request.joinAccountId(), userId, request.mainAccountId()));
	}
}
