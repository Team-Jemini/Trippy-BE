package org.scoula.mapper.exchange;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.controller.exchange.dto.response.AccountListDTO;
import org.scoula.domain.exchange.ExchangeRateVO;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ExchangeRateMapper {
    List<ExchangeRateVO> getExchangeRateList();


    /* 환전 기능 */
    List<AccountListDTO> getAccountList(String userId);

    Double findTodayRateByCurrencyCode(String currencyCode, LocalDateTime exchangeRateDate);
    Long findKrwBalanceByAccountId(String accountId);
    Double findForeignBalanceByAccountIdAndCurrency(String accountId, String currency);

}
