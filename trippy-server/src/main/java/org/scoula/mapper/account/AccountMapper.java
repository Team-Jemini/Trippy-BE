package org.scoula.mapper.account;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.account.AccountVO;

@Mapper
public interface AccountMapper {
    void insertAccount(AccountVO account);
}
