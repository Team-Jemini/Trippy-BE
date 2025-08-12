package org.scoula.service.transfer;

import static org.scoula.common.exception.enums.ErrorCode.*;

import org.scoula.common.exception.model.BadRequestException;
import org.scoula.controller.transfer.dto.request.GroupTransferRequestDTO;
import org.scoula.controller.transfer.dto.request.TransferRequestDTO;
import org.scoula.controller.transfer.dto.response.GroupTransferResponseDTO;
import org.scoula.controller.transfer.dto.response.TransferResponseDTO;
import org.scoula.domain.transaction.TransactionVO;
import org.scoula.mapper.account.AccountMapper;
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

	public GroupTransferResponseDTO groupTransfer(Long userId, GroupTransferRequestDTO request) {
		// 1.  요청한는 사람이 있는 유저인지 조회
		userService.validateUserExists(userId);
		// 2.  요청하는 사람이 모임장인지 조회
		userService.validateUserIsLeader(userId);
		// 3.  총 요청 금액이 잔액보다 적거나 같은지 확인
		// 4.  요청 받는 사람이 가입 된 유저인지 조회
		// 5. 요청 받는 사람의 계좌가 존재하는지 조회
		// 6. Vo 로 변환 후 입금
		// 7. 입금받는 유저 거래 내역 추가
		// 8. 돈 보내는 계좌 잔액 수정
		// 9. 돈 보내는 계좌 거래 내역 추가
		// 10. 입금받는 유저 알림 보내기
		// 11. 3번 부터 반복(선택한 모임원 수 만큼)
		// 12.  잔액 및 송금한 계좌 및 유저 아이디 유저 이름, 총 요청 금액 반환
	}
}
