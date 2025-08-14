package org.scoula.controller.exchange.dto;

import org.scoula.domain.exchange.ExchangeRateVO;

import java.time.LocalDateTime;

public record ExchangeRateDTO(
	Long exchangeRateId,
	String currencyCode,
	String currencyName,
	Double baseExchangeRate,
	Double rateBuy,
	Double rateSell,
	LocalDateTime exchangeRateDate
) {
	public static ExchangeRateDTO from(ExchangeRateVO vo) {
		return new ExchangeRateDTO(
				vo.getExchangeRateId(),
				vo.getCurrencyCode(),
				vo.getCurrencyName(),
				vo.getBaseExchangeRate(),
				vo.getRateBuy(),
				vo.getRateSell(),
				vo.getExchangeRateDate()
		);
	}
}

