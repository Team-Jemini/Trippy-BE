package org.scoula.domain.travel;

import org.scoula.domain.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class TravelReportVO extends BaseTime {
	private Long settlementId;
	private Long travelId;
	private Long userId;
	private String accountId;

	private String destination;
	private String travelBeginDate;
	private String travelEndDate;

	private Integer totalExpense;
	private Integer totalFood;
	private Integer totalActivity;
	private Integer totalAcc;
	private Integer totalTransport;
	private Integer totalShop;

	private String maxSpendingTitle;
	private Long maxSpendingAmount;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}