package org.scoula.mapper.transaction;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.transaction.TransactionVO;

@Mapper
public interface TransactionMapper {

	List<TransactionVO> getAccountTransaction(String accountId);
}
