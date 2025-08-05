package org.scoula.controller.exchange.dto;

import lombok.Data;

@Data
public class ExchRateAndBalanceResponse {
    private Long accountId;
    private String currencyCode;
    private String currencyName;
    private Long krwBalance;
    private Double foreignBalance;

}
