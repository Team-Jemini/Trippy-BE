package org.scoula.service.transfer;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.transfer.dto.request.TransferRequestDTO;
import org.scoula.controller.transfer.dto.response.TransferResponseDTO;
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

        // 돈 빠져나가는 account validate
        if (!accountMapper.existsByAccountId(requestDTO.fromAccountId())) {
            throw new ServerErrorException(ACCOUNT_NOT_FOUND);
        }

        // TransferRequestDTO -> TransactionVO로 변환


        // 목표 account가 우리 DB에 있는 거면 그 account balance 금액 증가
        if (accountMapper.existsByAccountId(requestDTO.toAccountId())) {
            // 해당 account에 거래 내역 추가
//            transactionMapper.saveTransaction();
        }
        // 돈 빠져나가는 account balance 금액 감소
        // 돈 빠져나가는 account의 거래 내역에 추가
        // 목표 account가 우리 DB에 있다면, 그 account에도 거래 내역 추가

    }
}
