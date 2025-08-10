package org.scoula.service.groupaccount;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.TrippyException;
import org.scoula.common.util.JwtTokenUtil;
import org.scoula.controller.groupAccount.dto.request.GroupAccountJoinRequestDTO;
import org.scoula.controller.groupAccount.dto.response.AcceptInviteResponseDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountJoinedResponseDTO;
import org.scoula.controller.groupAccount.dto.response.InviteResponseDTO;
import org.scoula.domain.account.member.AccountMemberVO;
import org.scoula.mapper.account.group.GroupAccountMapper;
import org.scoula.mapper.account.member.AccountMemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class InviteService {

	private final JwtTokenUtil jwtTokenUtil;
	private final String BASE_URL = "http://localhost:5173/?token=";
	private final GroupAccountMapper groupAccountmapper;
	private final AccountMemberMapper memberMapper;

	public InviteResponseDTO createInviteTokenURL(Long userId, String accountId, String accountName) {
		String userName = groupAccountmapper.selectUserName(userId);
		return new InviteResponseDTO(
			BASE_URL + jwtTokenUtil.createInviteToken(userId, userName, accountId, accountName));
	}

	public AcceptInviteResponseDTO parseInviteToken(Long userId, String token) {

		AcceptInviteResponseDTO response = jwtTokenUtil.parseInviteToken(token);

		validateUserNotAlreadyJoined(userId, response.accountId());

		return response;
	}

	/***
	 * 모임계좌에 참여
	 * @param userId
	 * @param request
	 * 토큰 분해
	 * 계좌가 있는지 체크
	 * 해지된 계좌인지 체크
	 * 계좌가 모임계좌이지 체크
	 * 사용자가 참여한 계좌인지 체크
	 */
	@Transactional
	public GroupAccountJoinedResponseDTO joinGroupAccount(Long userId, GroupAccountJoinRequestDTO request) {

		AcceptInviteResponseDTO response = jwtTokenUtil.parseInviteToken(request.token());

		validateAccountExistence(response.accountId());

		checkAccountDeletionStatus(response.accountId());

		validateAccountIsGroupAccount(response.accountId());

		validateUserNotAlreadyJoined(userId, response.accountId());

		groupAccountmapper.groupAccountJoin(
			AccountConverter.toAccountMemberVO(response.accountId(), userId, request.mainAccountId()));

		log.info("joinGroupAccount 가입 성공");
		AccountMemberVO memberVO = memberMapper.selectMemberInfo(userId, response.accountId());

		log.info("==============================");
		log.info("memberVO: {}", memberVO);
		return new GroupAccountJoinedResponseDTO(memberVO.getAccountId(), response.accountName(),
			memberVO.getCreatedAt());
	}

	private void validateAccountExistence(String accountId) {
		int accountCheckedCount = groupAccountmapper.existsAccountById(accountId);
		if (accountCheckedCount == 0) {
			throw new TrippyException(ErrorCode.ACCOUNT_NOT_FOUND);
		}
	}

	private void validateAccountIsGroupAccount(String accountId) {
		int accountCheckedCount = groupAccountmapper.validateAccountIsGroupAccount(accountId);
		if (accountCheckedCount == 0) {
			throw new TrippyException(ErrorCode.NOT_GROUP_ACCOUNT);
		}
	}

	private void checkAccountDeletionStatus(String accountId) {
		int accountCheckedCount = groupAccountmapper.checkAccountDeletionStatus(accountId);
		if (accountCheckedCount == 0) {
			throw new TrippyException(ErrorCode.ACCOUNT_ALREADY_DELETED);
		}
	}

	private void validateUserNotAlreadyJoined(Long userId, String accountId) {
		int userCheckedCount = groupAccountmapper.searchJoinUser(userId, accountId);
		if (userCheckedCount > 0) {
			throw new TrippyException(ErrorCode.ALREADY_INVITED);
		}
	}

}
