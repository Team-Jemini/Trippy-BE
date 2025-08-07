package org.scoula.mapper.transaction;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.transaction.TransactionVO;

@Mapper
public interface TransactionMapper {

	void saveTransaction(AccountVO account); // Todo: TransactionVO 로 바꿀 것
	List<TransactionVO> getAccountTransaction(String accountId);
}
