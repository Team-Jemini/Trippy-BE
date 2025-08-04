package org.scoula.controller.exchange.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExchangeRateDTO {
    private Long exchangeRateId;
    private String currencyCode;
    private String currencyName;
    private Double baseExchangeRate;
    private Double rateBuy;
    private Double rateSell;
    private LocalDateTime exchangeRateDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
