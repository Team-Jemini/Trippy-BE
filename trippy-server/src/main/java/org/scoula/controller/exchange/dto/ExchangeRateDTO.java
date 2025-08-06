package org.scoula.controller.exchange.dto;

import java.time.LocalDateTime;

public record ExchangeRateDTO(
	Long exchangeRateId,
	String currencyCode,
	String currencyName,
	Double baseExchangeRate,
	Double rateBuy,
	Double rateSell,
	LocalDateTime exchangeRateDate) {
}

