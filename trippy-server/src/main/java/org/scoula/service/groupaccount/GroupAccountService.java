package org.scoula.service.groupaccount;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.TrippyException;
import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.controller.groupAccount.dto.request.SettlementRequestDTO;
import org.scoula.controller.groupAccount.dto.response.AccountTransactionResponseDTO;
import org.scoula.controller.groupAccount.dto.response.DailyAccountTransactionDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountCreateResponseDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountDetailResponseDTO;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.account.group.GroupAccountVO;
import org.scoula.domain.account.member.Role;
import org.scoula.domain.notification.NotiType;
import org.scoula.domain.notification.NotificationVO;
import org.scoula.domain.transaction.TransactionVO;
import org.scoula.mapper.account.group.GroupAccountMapper;
import org.scoula.mapper.account.member.AccountMemberMapper;
import org.scoula.mapper.transaction.TransactionMapper;
import org.scoula.service.user.UserService;
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
	private final AccountMemberMapper memberMapper;
	private final UserService userService;

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

		isAccountValid(vo);

		checkAccountDeletionStatus(vo);

		List<TransactionVO> transaction = transactionMapper.getAccountTransaction(accountId);

		return AccountConverter.toGroupAccountDetailResponseDTO(vo, transaction);
	}

	private static void isAccountValid(GroupAccountVO vo) {
		if (vo == null) {
			throw new TrippyException(ErrorCode.ACCOUNT_NOT_FOUND);
		}
	}

	private static void checkAccountDeletionStatus(GroupAccountVO vo) {
		if (vo.getIsDeleted() == DeletedStatus.Y) {
			throw new TrippyException(ErrorCode.ACCOUNT_ALREADY_DELETED);
		}
	}

	/***
	 * 모임원들에게 정산요청
	 * @param userId
	 * @param request
	 *
	 * 모임주가 요청했는지 확인
	 * title 작성
	 * content 작성
	 * notificationVO 생성
	 * notification 테이블에 저장
	 */
	@Transactional
	public void sendSettlementRequest(Long userId, SettlementRequestDTO request) {
		isGroupAccountLeader(userId);

		List<NotificationVO> notifications = getNotificationVOList(request, getTitle(userId), getContent(request));

		groupAccountMapper.sendSettlementRequests(notifications);
	}

	private static List<NotificationVO> getNotificationVOList(SettlementRequestDTO request, String title,
		String content) {
		List<NotificationVO> notifications = request.memberList().stream()
			.map(member -> NotificationVO.builder()
				.userId(member.userId())
				.title(title)
				.content(content)
				.notiType(NotiType.REQUEST)
				.amount(request.amount())
				.build())
			.toList();
		return notifications;
	}

	private static String getContent(SettlementRequestDTO request) {
		String content =
			request.accountName() + " 계좌에서 " + request.accountId() + "로 " + request.amount() + "원을 정산 요청하였습니다";
		return content;
	}

	private String getTitle(Long userId) {
		String title = groupAccountMapper.selectUserName(userId) + "님이 정산을 요청 했습니다";
		return title;
	}

	private void isGroupAccountLeader(Long userId) {
		if (!memberMapper.isGroupAccountLeader(userId)) {
			throw new TrippyException(ErrorCode.NOT_GROUP_ACCOUNT_LEADER_EXCEPTION);
		}
	}

	public List<DailyAccountTransactionDTO> filterAccountTransactions(String accountId, Long userId,
		String transactionType) {

		isAccountuserValid(accountId, userId);

		List<AccountTransactionResponseDTO> flatList;

		if (transactionType.equals("ALL")) {
			flatList = AccountConverter.toTransactionResponseDTOList(
				transactionMapper.getAccountTransaction(accountId));
		} else {
			flatList = AccountConverter.toTransactionResponseDTOList(
				transactionMapper.filterAccountTransactions(accountId, transactionType));
		}

		// createdAt 기준으로 날짜별 그룹핑
		Map<LocalDate, List<AccountTransactionResponseDTO>> grouped = flatList.stream()
			.collect(
				Collectors.groupingBy(tx -> tx.createdAt().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

		return grouped.entrySet().stream()
			.map(entry -> new DailyAccountTransactionDTO(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	private void isAccountuserValid(String accountId, Long userId) {
		if (!groupAccountMapper.isGroupAccountUser(userId, accountId)) {
			throw new TrippyException(ErrorCode.ACCOUNT_NOT_FOUND);
		}
	}

	public List<GroupAccountDTO> getGroupAccountsList(Long userId) {
		userService.validateUserExists(userId);

		List<GroupAccountDTO> accounts = groupAccountMapper.findAllByUserIdOrderByUpdatedAt(userId).stream()
			.map(vo -> GroupAccountDTO.from(vo, userId))
			.toList();

		return accounts;
	}
}
