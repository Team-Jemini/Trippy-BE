package org.scoula.controller.exchange.dto.response;

public record ExchangeChangeRateDTO(
	String currencyName,
	Double todayExchangeRate,
	String upOrDown,
	Double changeAmount,
	Double changePercentage
) {
}
