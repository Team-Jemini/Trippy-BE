package org.scoula.service.transfer;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.transfer.dto.request.TransferRequestDTO;
import org.scoula.controller.transfer.dto.response.TransferResponseDTO;
import org.scoula.domain.transaction.TransactionVO;
import org.scoula.mapper.transaction.TransactionMapper;
import org.springframework.stereotype.Service;

import org.scoula.mapper.account.AccountMapper;
import org.scoula.service.user.UserService;
import org.scoula.common.exception.model.ServerErrorException;
import static org.scoula.common.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final UserService userService;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    public TransferResponseDTO transfer(final Long userId, final TransferRequestDTO requestDTO) {
        // userID validate
        userService.validateUserExists(userId);

        // 출금 계좌 validate
        if (!accountMapper.existsByAccountId(requestDTO.fromAccountId())) {
            throw new ServerErrorException(ACCOUNT_NOT_FOUND);
        }

        // 출금 계좌 잔액 확인
        Long fromBalance = accountMapper.findBalanceByAccountId(requestDTO.fromAccountId());
        if (fromBalance < requestDTO.amount()) {
            throw new ServerErrorException(LACK_BALANCE_EXCEPTION);
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
}
