package org.scoula.mapper.account;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.account.AccountVO;

@Mapper
public interface AccountMapper {
	void saveAccount(AccountVO account);

	boolean existsByAccountId(String accountId);

	List<AccountVO> findAllByUserIdOrderByUpdatedAt(Long userId);

	AccountVO getPersonalAccountDetail(@Param("accountId") String accountId, @Param("userId") Long userId);

	boolean isAccountUser(@Param("userId") Long userId, @Param("accountId") String accountId);

	Long findBalanceByAccountId(String accountId);

	void updateBalance(@Param("accountId") String accountId, @Param("balance") Long balance);

	boolean checkAccountDeletionStatus(String accountId);
}
