package org.scoula.mapper.exchange;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.exchange.AccountListVO;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.domain.exchange.ForeignAccountBalanceVO;

import java.util.List;

@Mapper
public interface ExchangeRateMapper {
    List<ExchangeRateVO> getExchangeRateList();

    /**
     * 환전 기능
     */
    List<AccountListVO> getAccountList(Long userId);

    ExchangeRateVO findTodayRateByCurrencyCode(String currencyCode);
    AccountListVO findKrwBalanceByAccountId(String accountId);
    Double findForeignBalanceByAccountIdAndCurrency(@Param("userId") String userId, @Param("currencyCode") String currencyCode);

}
