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
public class ExchangeLogVO extends BaseTime {
	private Long exchangeId;
	private String accountId;
	private Long exchangeAmountKrw;
	private Long exchangeAmountForeign;
	private Long exchangeRateExchanged;
	private String exchangeNation;
	private String currencyCode;
	private ExchangeType exchangeType;
	private LocalDateTime exchangeAt;
}