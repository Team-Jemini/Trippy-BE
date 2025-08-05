package org.scoula.external.exchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.scoula.domain.exchange.ExchangeRateVO;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true) // 필요없는 json 데이터 제거
public record ExchangeRateApiDTO (
    Long exchangeRateId,

    @JsonProperty("cur_unit")
    String currencyCode,

    @JsonProperty("cur_nm")
    String currencyName,

    @JsonProperty("deal_bas_r")
    String baseExchangeRate, // String -> double

    Double rateBuy,
    Double rateSell,
    LocalDateTime exchangeRateDate
) {

    public double getExchangeRateDouble(String baseExchangeRate) {
        String withoutComma = baseExchangeRate.replace(",", "");
        return Double.parseDouble(withoutComma);
    }

    public ExchangeRateVO toExchangeRateVO(String baseExchangeRate) {
        return ExchangeRateVO.builder()
            .exchangeRateId(exchangeRateId)
            .currencyCode(currencyCode)
            .currencyName(currencyName)
            .baseExchangeRate(getExchangeRateDouble(baseExchangeRate))
            .rateBuy(getExchangeRateDouble(baseExchangeRate))
            .rateSell(getExchangeRateDouble(baseExchangeRate))
            .exchangeRateDate(exchangeRateDate)
            .build();
    }

}