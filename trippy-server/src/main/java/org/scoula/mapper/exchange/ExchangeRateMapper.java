package org.scoula.mapper.exchange;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.exchange.AccountListVO;
import org.scoula.domain.exchange.ExchangeRateVO;

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

//    void exchange(@Param("krwAmount") String krwAmount,
//    			  @Param("foreignAmount") String foreignAmount,
//    			  @Param("currencyCode") String currencyCode,
//    			  @Param("krwAccountId") String krwAccountId,
//    			  @Param("foreignAccountId") String foreignAccountId,
//    			  @Param("userId") String userId);
//}

    boolean insertNewTransactionKrw(@Param("amount") Long KrwAmount,
    						  @Param("accountId") String accountId,
                                 @Param("userId") Long userId);

    boolean updateForeignAmount(@Param("foreignAmount") double foreignAmount,
                                @Param("accountId") String foreignAccountId,
                                @Param("currencyCode") String currencyCode);

    boolean updateKrwAmount(@Param("krwAmount") Long krwAmount,
                             @Param("accountId") String krwAccountId,
                             @Param("userId") Long userId);

}