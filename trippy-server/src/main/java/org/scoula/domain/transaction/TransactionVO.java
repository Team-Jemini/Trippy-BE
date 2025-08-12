package org.scoula.domain.transaction;

import org.scoula.controller.transfer.dto.request.GroupTransferRequestDTO;
import org.scoula.controller.transfer.dto.request.TransferMembersListRequestDTO;
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

	public static TransactionVO fromForGroupDeposit(GroupTransferRequestDTO request,
		TransferMembersListRequestDTO member, Long balanceAfter) {
		return TransactionVO.builder()
			.userId(member.userId())
			.accountId(member.mainAccountId())
			.transactionType(TransactionType.DEPOSIT)
			.amount(request.amount())
			.title(request.fromAccountName() != null ? request.fromAccountName() + "계좌에서 입금" : "입금")
			.category(TransactionCategory.INCOME)
			.balanceAfter(balanceAfter)
			.status(TransactionStatus.SUCCESS)
			.currencyCode(request.currencyCode())
			.build();

	}

	public static TransactionVO fromForGroupWithdraw(Long userId, GroupTransferRequestDTO request,
		TransferMembersListRequestDTO member, Long balanceAfter) {
		return TransactionVO.builder()
			.userId(userId)
			.accountId(request.fromAccountId())
			.transactionType(TransactionType.WITHDRAW)
			.amount(request.amount())
			.title("모임원 " + member.userName() + " 계좌로 출금")
			.category(TransactionCategory.OTHER)
			.balanceAfter(balanceAfter)
			.status(TransactionStatus.SUCCESS)
			.currencyCode(request.currencyCode())
			.build();

	}
}