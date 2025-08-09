package org.scoula.service.account;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.scoula.common.exception.enums.ErrorCode.*;
import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.ServerErrorException;
import org.scoula.common.exception.model.TrippyException;

import org.scoula.controller.account.dto.request.AccountRequestDTO;
import org.scoula.controller.account.dto.response.AccountResponseDTO;
import org.scoula.controller.account.dto.response.PersonalAccountDetailResponseDTO;
import org.scoula.controller.groupAccount.dto.response.AccountTransactionResponseDTO;
import org.scoula.controller.groupAccount.dto.response.DailyAccountTransactionDTO;

import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.transaction.TransactionVO;

import org.scoula.mapper.account.AccountMapper;
import org.scoula.mapper.transaction.TransactionMapper;
import org.scoula.service.groupaccount.AccountConverter;
import org.scoula.service.user.UserService;
import org.scoula.external.codef.accounts.service.CodefAccountService;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	final AccountMapper accountMapper;
	final TransactionMapper transactionMapper;
	private final CodefAccountService codefAccountService;
	private final UserService userService;

	public PersonalAccountDetailResponseDTO getPersonalAccountDetail(String accountId, Long userId) {

		AccountVO vo = accountMapper.getPersonalAccountDetail(accountId, userId);

		isAccountValid(vo);

		checkAccountDeletionStatus(vo);

		List<TransactionVO> transaction = transactionMapper.getAccountTransaction(accountId);

		return AccountConverter.toPersonalAccountDetailResponseDTO(vo, transaction);
	}

	private static void isAccountValid(AccountVO vo) {
		if (vo == null) {
			throw new TrippyException(ErrorCode.ACCOUNT_NOT_FOUND);
		}
	}

	private static void checkAccountDeletionStatus(AccountVO vo) {
		if (vo.getIsDeleted() == DeletedStatus.Y) {
			throw new TrippyException(ErrorCode.ACCOUNT_ALREADY_DELETED);
		}
	}

	public List<DailyAccountTransactionDTO> filterAccountTransactions(String accountId, Long userId,
		String transactionType) {

		isAccountuserValid(accountId, userId);

		List<AccountTransactionResponseDTO> flatList;

		if (transactionType.equals("ALL")) {
			flatList = AccountConverter.toTransactionResponseDTOList(
				transactionMapper.getAccountTransaction(accountId));
		} else {
			flatList = AccountConverter.toTransactionResponseDTOList(
				transactionMapper.filterAccountTransactions(accountId, transactionType));
		}

		// createdAt 기준으로 날짜별 그룹핑
		Map<LocalDate, List<AccountTransactionResponseDTO>> grouped = flatList.stream()
			.collect(
				Collectors.groupingBy(tx -> tx.createdAt().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

		return grouped.entrySet().stream()
			.map(entry -> new DailyAccountTransactionDTO(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	private void isAccountuserValid(String accountId, Long userId) {
		if (!accountMapper.isAccountUser(userId, accountId)) {
			throw new TrippyException(ErrorCode.ACCOUNT_NOT_FOUND);
		}
	}

	public List<AccountResponseDTO> getAccountsList(final Long userId) {
		userService.validateUserExists(userId);

		List<AccountResponseDTO> accounts = accountMapper.findAllByUserIdOrderByUpdatedAt(userId).stream()
			.map(vo -> AccountResponseDTO.from(vo, userId))
			.toList();

		if (accounts.isEmpty()) {
			throw new ServerErrorException(GET_ACCOUNTS_LIST_FAILED);
		}

		return accounts;
	}

	public List<AccountResponseDTO> getCodefAccounts(final Long userId) {
		userService.validateUserExists(userId);

		String accountListJson = codefAccountService.getAccountList();

		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> responseMap = mapper.readValue(accountListJson, Map.class);

			List<Map<String, Object>> accountList = (List<Map<String, Object>>)
				((Map<String, Object>)responseMap.get("data")).get("resDepositTrust");

			return accountList.stream()
				.map(accountMap -> AccountResponseDTO.from(AccountVO.fromCodefResponse(accountMap, userId), userId))
				.toList();

		} catch (IOException e) {
			throw new ServerErrorException(GET_ACCOUNTS_LIST_FAILED);
		}
	}

	@Transactional
	public void saveAccounts(final Long userId, List<AccountRequestDTO> requestList) {
		userService.validateUserExists(userId);
		for (AccountRequestDTO accountRequestDTO : requestList) {
			if (accountMapper.existsByAccountId(accountRequestDTO.accountId())) {
				log.info("중복 계좌 건너뜀: {}", accountRequestDTO.accountId());
				continue;
			}
			accountMapper.saveAccount(AccountVO.from(accountRequestDTO, userId));
		}
    }
}
