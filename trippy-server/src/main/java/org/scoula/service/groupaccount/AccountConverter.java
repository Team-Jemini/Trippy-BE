package org.scoula.service.groupaccount;

import java.util.List;
import java.util.stream.Collectors;

import org.scoula.controller.account.dto.response.PersonalAccountDetailResponseDTO;
import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.controller.groupAccount.dto.response.AccountTransactionResponseDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountDetailResponseDTO;
import org.scoula.domain.account.AccountForeign;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.account.group.GroupAccountVO;
import org.scoula.domain.account.member.AccountMemberVO;
import org.scoula.domain.account.member.Role;
import org.scoula.domain.transaction.TransactionVO;

public class AccountConverter {

	public static AccountVO toAccountVO(String accountId, Long userId, AccountType accountType,
		GroupAccountCreateRequestDTO request) {
		return AccountVO.builder()
			.accountId(accountId)
			.userId(userId)
			.accountName(request.accountName())
			.accountType(accountType)
			.ownerId(userId)
			.balance(0L)
			.accountForeign(AccountForeign.kor)
			.isDeleted(DeletedStatus.N)
			.build();
	}

	public static AccountMemberVO toAccountMemberVO(String accountId, Long userId, String mainAccountId, Role role) {
		return AccountMemberVO.builder()
			.accountId(accountId)
			.userId(userId)
			.role(role)
			.mainAccountId(mainAccountId)
			.build();
	}

	public static AccountMemberVO toAccountMemberVO(String accountId, Long userId, String mainAccountId) {
		return AccountMemberVO.builder()
			.accountId(accountId)
			.userId(userId)
			.role(Role.member)
			.mainAccountId(mainAccountId)
			.build();
	}

	public static AccountTransactionResponseDTO toTransactionResponseDTO(TransactionVO vo) {
		return new AccountTransactionResponseDTO(
			vo.getTransactionId(),
			vo.getTransactionType().name(),
			vo.getAmount(),
			vo.getTitle(),
			vo.getCategory().name(),
			vo.getStatus().name(),
			vo.getCurrencyCode(),
			vo.getCreatedAt()
		);
	}

	public static List<AccountTransactionResponseDTO> toTransactionResponseDTOList(List<TransactionVO> voList) {
		return voList.stream()
			.map(AccountConverter::toTransactionResponseDTO)
			.collect(Collectors.toList());
	}

	public static GroupAccountDetailResponseDTO toGroupAccountDetailResponseDTO(
		GroupAccountVO accountVO, List<TransactionVO> transactionVOs) {

		List<AccountTransactionResponseDTO> transactions = toTransactionResponseDTOList(transactionVOs);

		return new GroupAccountDetailResponseDTO(
			accountVO.getUserId(),
			accountVO.getAccountId(),
			accountVO.getAccountName(),
			accountVO.getAccountType(),
			accountVO.getOwnerId(),
			accountVO.getBalance(),
			accountVO.getAccountForeign(),
			accountVO.getIsDeleted(),
			accountVO.getRole(),
			transactions
		);
	}

	public static PersonalAccountDetailResponseDTO toPersonalAccountDetailResponseDTO(
		AccountVO accountVO, List<TransactionVO> transactionVOs) {

		List<AccountTransactionResponseDTO> transactions = toTransactionResponseDTOList(transactionVOs);

		return new PersonalAccountDetailResponseDTO(
			accountVO.getUserId(),
			accountVO.getAccountId(),
			accountVO.getAccountName(),
			accountVO.getAccountType(),
			accountVO.getOwnerId(),
			accountVO.getBalance(),
			accountVO.getAccountForeign(),
			accountVO.getIsDeleted(),
			transactions
		);
	}
}