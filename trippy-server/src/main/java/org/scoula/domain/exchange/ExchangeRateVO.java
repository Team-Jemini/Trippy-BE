package org.scoula.domain.exchange;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
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
	private String currencyName;
	private Double baseExchangeRate;
	private Double rateBuy;
	private Double rateSell;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime exchangeRateDate; //환율 고시날짜?

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
}