package org.scoula.service.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.scoula.domain.transaction.TransactionVO;
import org.scoula.mapper.transaction.TransactionMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionMapper transactionMapper;

	public List<TransactionVO> getUserExpenseTransactions(Long userId, String accountId, LocalDateTime startDate,
		LocalDateTime endDate) {
		return transactionMapper.findExpenseTransactionsByAccountId(userId, accountId, startDate, endDate);
	}

	public TransactionVO getTransaction(Long transactionId) {
		return transactionMapper.findByTransactionId(transactionId);
	}
}
