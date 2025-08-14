package org.scoula.controller.exchange.dto.response;

public record ExchangeBalanceDTO(
	String currencyCode,
	Double rate,
	Long krwBalance,
	Double foreignBalance,
	String currencyName
) {
	public static ExchangeBalanceDTO from(
		String currencyCode,
		Double rate,
		Long krwBalance,
		Double foreignBalance,
		String currencyName
	) {
		return new ExchangeBalanceDTO(currencyCode, rate, krwBalance, foreignBalance, currencyName);
	}
}