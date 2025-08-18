package org.scoula.service.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.scoula.domain.transaction.TransactionVO;
import org.scoula.external.chatGPT.GPTService;
import org.scoula.mapper.transaction.TransactionMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionMapper transactionMapper;
	private final GPTService gptService;

	public int classifyAndSaveMissingCategories(int limit) {
		int updated = 0;
		var targets = transactionMapper.findUncategorized(limit);
		for (var t : targets) {
			String title = t.getTitle();
			if (title == null || title.isBlank()) {
				updated += transactionMapper.updateCategoryById(t.getTransactionId(), "OTHER");
				continue;
			}
			String cat = gptService.suggestCategory(title);
			updated += transactionMapper.updateCategoryById(t.getTransactionId(), cat);
		}
		return updated;
	}

	public List<TransactionVO> getUserExpenseTransactions(Long userId, String accountId, LocalDateTime startDate,
		LocalDateTime endDate) {
		return transactionMapper.findExpenseTransactionsByAccountId(userId, accountId, startDate, endDate);
	}

	public TransactionVO getTransaction(Long transactionId) {
		return transactionMapper.findByTransactionId(transactionId);
	}


}
