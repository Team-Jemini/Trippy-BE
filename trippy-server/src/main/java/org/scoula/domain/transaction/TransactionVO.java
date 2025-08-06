package org.scoula.domain.transaction;

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
public class TransactionVO extends BaseTime {
	private String accountId;
	private Long userId;
	private Long transactionId;
	private TransactionType transactionType;  // deposit or withdraw
	private Long amount;
	private String title;
	private TransactionCategory category;     // 식비, 액티비티 등
	private String latitude;
	private String longitude;
	private Long balanceAfter;                // 거래이후 잔액
	private TransactionStatus status;         // SUCCESS, PENDING, FAIL
	private String currencyCode;
}