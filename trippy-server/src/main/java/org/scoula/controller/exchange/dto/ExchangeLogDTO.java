package org.scoula.controller.exchange.dto;

import org.scoula.domain.exchange.ExchangeType;

import java.time.LocalDateTime;

public record ExchangeLogDTO(
	Long exchangeId,
	String accountId,
	Long amountKRW,
	Long amountForeign,
	Long appliedExchangeRate,
	String nation,
	String currencyCode,
	ExchangeType exchangeType,
	LocalDateTime exchangeDate,
	LocalDateTime createdAt,
	LocalDateTime updatedAt) {
}
