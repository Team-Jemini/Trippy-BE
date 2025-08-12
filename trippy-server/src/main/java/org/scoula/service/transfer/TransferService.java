package org.scoula.service.transfer;

import static org.scoula.common.exception.enums.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.scoula.common.exception.model.BadRequestException;
import org.scoula.controller.transfer.dto.request.GroupTransferRequestDTO;
import org.scoula.controller.transfer.dto.request.TransferMembersListRequestDTO;
import org.scoula.controller.transfer.dto.request.TransferRequestDTO;
import org.scoula.controller.transfer.dto.response.GroupTransferResponseDTO;
import org.scoula.controller.transfer.dto.response.TransferMembersListResponseDTO;
import org.scoula.controller.transfer.dto.response.TransferResponseDTO;
import org.scoula.domain.notification.NotificationVO;
import org.scoula.domain.transaction.TransactionVO;
import org.scoula.mapper.account.AccountMapper;
import org.scoula.mapper.account.group.GroupAccountMapper;
import org.scoula.mapper.notification.NotificationMapper;
import org.scoula.mapper.transaction.TransactionMapper;
import org.scoula.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferService {
	private final UserService userService;
	private final AccountMapper accountMapper;
	private final TransactionMapper transactionMapper;
	private final NotificationMapper notificationMapper;
	private final GroupAccountMapper groupAccountMapper;

	@Transactional
	public TransferResponseDTO transfer(final Long userId, final TransferRequestDTO requestDTO) {
		// userID validate
		userService.validateUserExists(userId);

		// 출금 계좌 validate
		if (!accountMapper.existsByAccountId(requestDTO.fromAccountId())) {
			throw new BadRequestException(ACCOUNT_NOT_FOUND);
		}

		// 출금 계좌 잔액 확인
		Long fromBalance = accountMapper.findBalanceByAccountId(requestDTO.fromAccountId());
		if (fromBalance < requestDTO.amount()) {
			throw new BadRequestException(LACK_BALANCE_EXCEPTION);
		}

		// 출금 계좌 잔액 차감
		Long updatedFromBalance = fromBalance - requestDTO.amount();
		accountMapper.updateBalance(requestDTO.fromAccountId(), updatedFromBalance);

		// 출금 내역 저장
		transactionMapper.saveTransaction(TransactionVO.fromForWithdraw(userId, requestDTO, updatedFromBalance));

		// 수신 계좌가 우리 DB에 있는 경우, 해당 계좌의 잔액과 거래 내역 업데이트
		if (accountMapper.existsByAccountId(requestDTO.toAccountId())) {
			Long toBalance = accountMapper.findBalanceByAccountId(requestDTO.toAccountId());
			Long updatedToBalance = toBalance + requestDTO.amount();
			accountMapper.updateBalance(requestDTO.toAccountId(), updatedToBalance);

			transactionMapper.saveTransaction(TransactionVO.fromForDeposit(userId, requestDTO, updatedToBalance));
		}

		return new TransferResponseDTO(
			requestDTO.fromAccountId(),
			requestDTO.toAccountId(),
			requestDTO.amount(),
			updatedFromBalance,
			requestDTO.currencyCode()
		);
	}

	@Transactional
	public GroupTransferResponseDTO groupTransfer(Long userId, GroupTransferRequestDTO request) {

		// 송금 하는 사람과 모임계좌 사전 검증
		validateGroupTransferPreconditions(userId, request);

		//알림 저장소
		List<NotificationVO> noticeList = new ArrayList<>();

		// 멤버들한테 송금
		for (TransferMembersListRequestDTO member : request.memberList()) {
			processMemberTransfer(userId, request, member, noticeList);
		}
		// 17. 입금받는 유저 알림 저장
		saveDepositNotifications(noticeList);

		// 18.  잔액 및 송금한 계좌 및 유저 아이디 유저 이름, 총 요청 금액-return
		return buildGroupTransferResponseDTO(request);
	}

	private void processMemberTransfer(Long userId, GroupTransferRequestDTO request,
		TransferMembersListRequestDTO member, List<NotificationVO> noticeList) {

		// 송금 받는 사람 검증
		validateTransferRecipient(member);

		// 송금
		validateAndProcessMemberWithdrawal(userId, request, member);

		// 입금
		processGroupDepositForMember(request, member);

		// 알림 생성 및 리스트에 저장
		noticeList.add(
			NotificationVO.DepositNotification(member.userId(), request.fromAccountName(), request.amount()));
	}

	private void validateGroupTransferPreconditions(Long userId, GroupTransferRequestDTO request) {

		// 1.  요청한는 사람이 있는 유저인지 조회
		userService.validateUserExists(userId);

		// 2. 송금하는 계좌가 존재하는지 확인
		validateAccountExists(request.fromAccountId());

		// 3. 송금하는 계좌가 해지돼어있는지 확인
		checkAccountDeletionStatus(request.fromAccountId());

		// 4.  요청하는 사람이 모임장인지 조회
		userService.validateUserIsLeader(userId);

		//총 요청 금액이 잔액보다 적은지 확인
		validateSufficientBalance(request.fromAccountId(), request.amount() * request.memberList().size());
	}

	private void validateTransferRecipient(TransferMembersListRequestDTO member) {
		// 6. 요청 받는 사람이 가입 된 유저인지 조회
		userService.validateUserExists(member.userId());

		// 7. 요청 받는 사람의 계좌가 존재하는지 조회
		validateAccountExists(member.mainAccountId());

		// 8. 계좌가 해지돼어있는지 확인
		checkAccountDeletionStatus(member.mainAccountId());

		// 9. 입금 받는 사람의 대표계좌인지 조회
		validateUserMainAccountExists(member.userId(), member.mainAccountId());

	}

	private void validateAndProcessMemberWithdrawal(Long userId, GroupTransferRequestDTO request,
		TransferMembersListRequestDTO member) {

		// 910. 출금 계좌의 잔액 구하기
		Long fromBalance = accountMapper.findBalanceByAccountId(request.fromAccountId());

		// 11. 출금 계좌의 afterBalance 구하기10. 출금 계좌의 afterBalance 구하기
		Long fromAfterBalance = fromBalance - request.amount();

		// 12. 출금 계좌 잔액 수정
		accountMapper.updateBalance(request.fromAccountId(), fromAfterBalance);

		// 13. TransactionVO 로 변환, 출금 계좌 거래 내역 리스트에 추가
		TransactionVO withdrawVo = TransactionVO.fromForGroupWithdraw(userId, request, member, fromAfterBalance);
		transactionMapper.saveTransaction(withdrawVo);
	}

	private void processGroupDepositForMember(GroupTransferRequestDTO request, TransferMembersListRequestDTO member) {

		// 14. 입금 계좌의 잔액 구하기
		Long toBalance = accountMapper.findBalanceByAccountId(member.mainAccountId());

		// 15. 입금 계좌 afterBalance 구하기
		Long toAfterBalance = toBalance + request.amount();

		// 16. 입금 계좌 잔액 수정
		accountMapper.updateBalance(member.mainAccountId(), toAfterBalance);

		// 17. TransactionVO 로 변환후 입금 계좌 거래 내역 리스트에 추가
		TransactionVO depositVo = TransactionVO.fromForGroupDeposit(request, member, toAfterBalance);
		transactionMapper.saveTransaction(depositVo);
	}

	private void saveDepositNotifications(List<NotificationVO> noticeList) {
		for (NotificationVO notice : noticeList) {
			notificationMapper.saveNotification(notice);
		}
	}

	@NotNull
	private GroupTransferResponseDTO buildGroupTransferResponseDTO(GroupTransferRequestDTO request) {
		Long totalAmount = request.amount() * request.memberList().size();
		Long balance = accountMapper.findBalanceByAccountId(request.fromAccountId());

		List<TransferMembersListResponseDTO> memberList = new ArrayList<>();
		for (TransferMembersListRequestDTO member : request.memberList()) {
			memberList.add(
				new TransferMembersListResponseDTO(member.mainAccountId(), member.userId(), member.userName()));
		}

		LocalDateTime now = notificationMapper.selectNow();

		return new GroupTransferResponseDTO(request.fromAccountId(), request.fromAccountName(), totalAmount,
			balance, request.currencyCode(),
			now, memberList);
	}

	public void validateSufficientBalance(String fromAccountId, Long amount) {
		if (accountMapper.findBalanceByAccountId(fromAccountId) < amount) {
			throw new BadRequestException(LACK_BALANCE_EXCEPTION);
		}
	}

	public void validateAccountExists(String accountId) {
		if (!accountMapper.existsByAccountId(accountId)) {
			throw new BadRequestException(ACCOUNT_NOT_FOUND);
		}
	}

	public void checkAccountDeletionStatus(String accountId) {
		if (!accountMapper.checkAccountDeletionStatus(accountId)) {
			throw new BadRequestException(ACCOUNT_ALREADY_DELETED);
		}
	}

	public void validateUserMainAccountExists(Long userId, String accountId) {
		if (!groupAccountMapper.existsByUserIdAndMainAccountId(userId, accountId)) {
			throw new BadRequestException(ACCOUNT_NOT_USER_MAIN_ACCOUNT);
		}
	}
}
