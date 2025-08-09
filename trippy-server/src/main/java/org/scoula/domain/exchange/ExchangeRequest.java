package org.scoula.domain.exchange;

public record ExchangeRequest(
        Long krwAmount,
        String krwAccountId,
        Long userId,
        String currencyCode,
        String foreignAccountId,
        double foreignAmount
) { }
