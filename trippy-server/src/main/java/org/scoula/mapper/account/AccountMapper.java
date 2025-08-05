package org.scoula.mapper.account;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.account.AccountVO;

import java.util.List;

@Mapper
public interface AccountMapper {
    void insertAccount(AccountVO account);
    boolean existsByAccountId(String accountId);
    List<AccountVO> findAllByUserIdOrderByUpdatedAt(Long userId);
}
