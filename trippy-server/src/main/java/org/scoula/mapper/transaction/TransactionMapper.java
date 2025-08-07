package org.scoula.mapper.transaction;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.transaction.TransactionVO;

@Mapper
public interface TransactionMapper {

	void saveTransaction(TransactionVO transactionVO); // Todo: TransactionVO 로 바꿀 것
	List<TransactionVO> getAccountTransaction(String accountId);

	List<TransactionVO> filterAccountTransactions(@Param("accountId") String accountId,
		@Param("transactionType") String transactionType);
}
