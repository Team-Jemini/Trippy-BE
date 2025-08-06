package org.scoula.service.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.scoula.controller.exchange.dto.response.ExchRateAneBalanceDTO;
import org.scoula.controller.exchange.dto.response.ExchangeBalanceDTO;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.mapper.exchange.ExchangeRateMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

	private final ExchangeRateMapper exchangeRateMapper;

	/***
	 * DB에서 환율 데이터 조회
	 */
	public List<ExchangeRateVO> getExchangeRates() {
		return exchangeRateMapper.getExchangeRateList();
	}

	/***
	 * 환전
	 */
	public ExchangeBalanceDTO getRatesAndBalance(String currencyCode, Long accountId) {
		Double rate = exchangeRateMapper.findTodayRateByCurrencyCode(currencyCode);
		Long krwBalance = exchangeRateMapper.findKrwBalanceByAccountId(accountId);
		Double foreignBalance = exchangeRateMapper.findForeignBalanceByAccountIdAndCurrency(accountId, currencyCode);

		return ExchangeBalanceDTO.from(currencyCode, rate, krwBalance, foreignBalance);
	}
}