package org.scoula.domain.travel;

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
public class TravelReportVO extends BaseTime {
	private String accountId;
	private Long userId;
	private Long settlementId;
	private Long travelId;
	private LocalDateTime travelBegin;
	private LocalDateTime travelEnd;
	private Integer totalExpense;
	private Integer totalFood;
	private Integer totalActivity;
	private Integer totalAcc;
	private Integer totalTransport;
	private Integer totalShop;
}