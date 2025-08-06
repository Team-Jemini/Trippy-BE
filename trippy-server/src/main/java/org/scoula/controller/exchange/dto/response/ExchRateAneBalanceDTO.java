package org.scoula.controller.exchange.dto.response;

public record ExchRateAneBalanceDTO(
	Long accountId,
	String currencyCode,
	String currencyName,
	Long krwBalance,
	Double foreignBalance
) {
	public static ExchRateAneBalanceDTO from(Long accountId, String currencyCode, String currencyName, Long krwBalance,
		Double foreignBalance) {
		return new ExchRateAneBalanceDTO(accountId, currencyCode, currencyName, krwBalance, foreignBalance);
	}
}
