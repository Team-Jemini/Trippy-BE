package org.scoula.controller.exchange.dto.response;

public record ExchangeBalanceDTO(
	String currencyCode,
	Double rate,
	Long krwBalance,
	Double foreignBalance
) {
	public static ExchangeBalanceDTO from(
		String currencyCode,
		Double rate,
		Long krwBalance,
		Double foreignBalance
	) {
		return new ExchangeBalanceDTO(currencyCode, rate, krwBalance, foreignBalance);
	}
}