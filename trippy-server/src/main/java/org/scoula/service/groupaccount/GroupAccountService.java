package org.scoula.service.groupaccount;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.TrippyException;
import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountCreateResponseDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountDetailResponseDTO;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.account.group.GroupAccountVO;
import org.scoula.domain.account.member.Role;
import org.scoula.domain.transaction.TransactionVO;
import org.scoula.mapper.account.group.GroupAccountMapper;
import org.scoula.mapper.transaction.TransactionMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class GroupAccountService {

	private final GroupAccountMapper groupAccountMapper;
	private final TransactionMapper transactionMapper;

	/****
	 * 모임계좌 생성
	 * @param request
	 * @param userId
	 * @return
	 */
	@Transactional
	public GroupAccountCreateResponseDTO createGroupAccount(GroupAccountCreateRequestDTO request, Long userId) {
		int tryCount = 0;
		while (tryCount++ < 5) {
			String accountId = checkedCreateGroupId(userId);
			try {
				groupAccountMapper.createGroupAccount(
					AccountConverter.toAccountVO(accountId, userId, AccountType.group, request));

				groupAccountMapper.createGroupAccountMember(
					AccountConverter.toAccountMemberVO(accountId, userId, request.mainAccountId(), Role.leader));

				AccountVO account = groupAccountMapper.selectGroupAccountById(accountId);

				return new GroupAccountCreateResponseDTO(
					account.getAccountId(),
					account.getAccountName(),
					account.getCreatedAt());

			} catch (DuplicateKeyException e) {
				log.warn(ErrorCode.DUPLICATE_ACCOUNT_ID_EXCEPTION.getMessage());
			}
		}
		throw new RuntimeException(ErrorCode.ACCOUNT_CREATION_FAILED.getMessage());
	}

	/****
	 * 모임계좌 id생성(계좌번호) 17자리
	 * @param userId
	 * @return
	 */
	private String checkedCreateGroupId(Long userId) {
		int tryCount = 0;
		while (tryCount++ < 5) {
			String groupId = createGroupId(userId);
			int count = groupAccountMapper.existsGroupId(groupId);
			if (count == 0) {
				return groupId;
			}
		}
		throw new RuntimeException(ErrorCode.ACCOUNT_CREATION_FAILED.getMessage());
	}

	/****
	 * 모임계좌번호 생성
	 * @param userId
	 * @return
	 */
	private String createGroupId(Long userId) {
		String prePix = "0707";
		String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
		Long todayCount = groupAccountMapper.countGroupAccountsByDate(datePart);
		String sequencePart = String.format("%05d", todayCount + 1);
		String userIdStr = String.format("%04d", userId % 10000);
		String randomPart = String.format("%04d", ThreadLocalRandom.current().nextInt(0, 10000));

		return prePix + "-" + sequencePart + userIdStr + "-" + randomPart;
	}

	public GroupAccountDetailResponseDTO getGroupAccountDetail(String accountId, Long userId) {

		GroupAccountVO vo = groupAccountMapper.getGroupAccountDetail(accountId, userId);

		if (vo == null) {
			throw new TrippyException(ErrorCode.ACCOUNT_NOT_FOUND);
		}

		checkAccountDeletionStatus(vo);

		List<TransactionVO> transaction = transactionMapper.getAccountTransaction(accountId);

		return AccountConverter.toGroupAccountDetailResponseDTO(vo, transaction);
	}

	private static void checkAccountDeletionStatus(GroupAccountVO vo) {
		if (vo.getIsDeleted() == DeletedStatus.Y) {
			throw new TrippyException(ErrorCode.ACCOUNT_ALREADY_DELETED);
		}
	}
}
