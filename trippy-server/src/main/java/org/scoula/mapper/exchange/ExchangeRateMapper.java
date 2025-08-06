package org.scoula.mapper.exchange;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.exchange.ExchangeRateVO;

import java.util.List;

@Mapper
public interface ExchangeRateMapper {
    List<ExchangeRateVO> getExchangeRateList();


    /* 환전 기능 */
    Double findTodayRateByCurrencyCode(String currencyCode);
    Long findKrwBalanceByAccountId(Long accountId);
    Double findForeignBalanceByAccountIdAndCurrency(Long accountId, String currency);

}
