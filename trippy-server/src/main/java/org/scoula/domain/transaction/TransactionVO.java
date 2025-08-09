package org.scoula.domain.transaction;

import org.scoula.controller.transfer.dto.request.TransferRequestDTO;
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
	private Long transactionId;
	private Long userId;
	private String accountId;
	private TransactionType transactionType;  // deposit or withdraw
	private Long amount;
	private String title;
	private TransactionCategory category;     // 식비, 액티비티 등
	private String latitude;
	private String longitude;
	private Long balanceAfter;                // 거래이후 잔액
	private TransactionStatus status;         // SUCCESS, PENDING, FAIL
	private String currencyCode;

	public static TransactionVO fromForWithdraw(Long userId, TransferRequestDTO request, Long balanceAfter) {
		return new TransactionVO(
				null,
				userId,
				request.fromAccountId(),
				TransactionType.WITHDRAW,
				request.amount(),
				request.title(),
				TransactionCategory.OTHER,
				null,
				null,
				balanceAfter,
				TransactionStatus.SUCCESS,
				request.currencyCode()
		);
	}

	public static TransactionVO fromForDeposit(Long userId, TransferRequestDTO request, Long balanceAfter) {
		return new TransactionVO(
				null,
				userId,
				request.toAccountId(),
				TransactionType.DEPOSIT,
				request.amount(),
				request.title(),
				TransactionCategory.INCOME,
				null,
				null,
				balanceAfter,
				TransactionStatus.SUCCESS,
				request.currencyCode()
		);
	}
}