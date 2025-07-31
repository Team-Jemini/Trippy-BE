package org.scoula.domain.exchange;

import java.time.LocalDateTime;

import org.scoula.domain.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ExchangeRateVO extends BaseTime {
	private Long exchangeRateId;
	private String currencyCode;
	private Integer baseExchangeRate;
	private Integer exchangeRateBuy;
	private Integer exchangeRateSell;
	private LocalDateTime exchangeRateDate;
}