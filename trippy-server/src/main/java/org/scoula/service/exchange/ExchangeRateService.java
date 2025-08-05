package org.scoula.service.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.controller.exchange.dto.ExchRateAndBalanceResponse;
import org.scoula.controller.exchange.dto.ExchangeBalanceResponse;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.mapper.exchange.ExchangeRateMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateMapper mapper;

    /* DB에서 환율 데이터 가져오는 함수 */
    public List<ExchangeRateVO> getExchangeRates() {
        return mapper.getExchangeRateList();
    }

    /* 환전 기능 (작성 중) */
    public ExchRateAndBalanceResponse getRatesAndBalance(String currencyCode, Long accountId) {
        Double rate = mapper.findTodayRateByCurrencyCode(currencyCode);
        Long krwBalance = mapper.findKrwBalanceByAccountId(accountId);
        Double foreignBalance = mapper.findForeignBalanceByAccountIdAndCurrency(accountId, currencyCode);

        return new ExchangeBalanceResponse(currencyCode, rate, krwBalance, foreignBalance);
    }

    public ExchRateAndBalanceResponse doExchange(ExchRateAndBalanceResponse exchRateAndBalanceResponse) {
        return exchRateAndBalanceResponse;
    }
}