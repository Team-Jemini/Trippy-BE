package org.scoula.domain.account;

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
public class ForeignAccountBalanceVO extends BaseTime {
	private Long accountId;
	private Long balanceId;
	private String currencyCode;
	private Long balance;
}