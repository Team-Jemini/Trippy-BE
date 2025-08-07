package org.scoula.mapper.account;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.account.AccountVO;

import java.util.List;

@Mapper
public interface AccountMapper {
    void saveAccount(AccountVO account);
    boolean existsByAccountId(String accountId);
    List<AccountVO> findAllByUserIdOrderByUpdatedAt(Long userId);

    void updateBalance(@Param("accountId") String accountId, @Param("balance") Long balance);
}
