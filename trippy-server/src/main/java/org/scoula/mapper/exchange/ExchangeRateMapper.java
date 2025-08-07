package org.scoula.mapper.exchange;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.exchange.AccountListVO;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.domain.exchange.ForeignAccountBalanceVO;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ExchangeRateMapper {
    List<ExchangeRateVO> getExchangeRateList();


    /* 환전 기능 */
    List<AccountListVO> getAccountList(Long userId);

    ExchangeRateVO findTodayRateByCurrencyCode(String currencyCode, LocalDateTime exchangeRateDate);
    AccountVO findKrwBalanceByAccountId(String accountId);
    ForeignAccountBalanceVO findForeignBalanceByAccountIdAndCurrency(String accountId, String currency);

}
