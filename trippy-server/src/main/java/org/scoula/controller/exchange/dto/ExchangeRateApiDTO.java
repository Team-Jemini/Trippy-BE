package org.scoula.controller.exchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 필요없는 json 데이터 제외
public class ExchangeRateApiDTO {


    private Long exchangeRateId;

    @JsonProperty("cur_unit")
    private String currencyCode;

    @JsonProperty("cur_nm")
    private String currencyName;

    @JsonProperty("deal_bas_r")
    private String baseExchangeRate;
    // String으로 받고 나중에 int로 변환

    private Double rateBuy;
    private Double rateSell;
    private LocalDateTime exchangeRateDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
