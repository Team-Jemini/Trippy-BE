package org.scoula.domain.exchange;

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
public class ExchangeLogVO extends BaseTime {
	private Long exchangeId;
	private String accountId;
	private Long amountKRW;
	private Long amountForeign;
	private Long appliedExchangeRate;
	private String nation;
	private String currencyCode;
	private ExchangeType exchangeType;
}