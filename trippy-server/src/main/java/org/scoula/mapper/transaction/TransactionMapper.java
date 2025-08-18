package org.scoula.mapper.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.transaction.TransactionVO;

@Mapper
public interface TransactionMapper {

	void saveTransaction(TransactionVO transactionVO);

	List<TransactionVO> getAccountTransaction(String accountId);

	List<TransactionVO> filterAccountTransactions(@Param("accountId") String accountId,
		@Param("transactionType") String transactionType);

	List<TransactionVO> findExpenseTransactionsByAccountId(@Param("userId") Long userId,
		@Param("accountId") String accountId, @Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);

	TransactionVO findByTransactionId(Long transactionId);

	List<TransactionVO> findUncategorized(@Param("limit") int limit);
	int updateCategoryById(@Param("transactionId") Long transactionId,
		@Param("category") String category);
}
