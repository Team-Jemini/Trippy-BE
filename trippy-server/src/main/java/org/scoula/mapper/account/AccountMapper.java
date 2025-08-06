package org.scoula.mapper.account;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.account.AccountVO;

@Mapper
public interface AccountMapper {

	AccountVO getPersonalAccountDetail(@Param("accountId") String accountId, @Param("userId") Long userId);
}
