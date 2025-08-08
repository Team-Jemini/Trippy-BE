package org.scoula.service.account;

import java.io.IOException;
import java.util.List;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.TrippyException;
import org.scoula.controller.account.dto.response.PersonalAccountDetailResponseDTO;
import org.scoula.controller.groupAccount.dto.response.AccountTransactionResponseDTO;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.transaction.TransactionVO;
import org.scoula.mapper.account.AccountMapper;
import org.scoula.mapper.transaction.TransactionMapper;
import org.scoula.service.groupaccount.AccountConverter;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.scoula.controller.account.dto.response.AccountResponseDTO;
import org.scoula.common.exception.model.ServerErrorException;
import org.scoula.domain.account.AccountType;
import org.scoula.external.codef.accounts.service.CodefAccountService;
import org.scoula.service.user.UserService;
import static org.scoula.common.exception.enums.ErrorCode.*;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
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

	public List<AccountTransactionResponseDTO> filterAccountTransactions(String accountId, Long userId,
		String transactionType) {

		isAccountuserValid(accountId, userId);

		if (transactionType.equals("ALL")) {
			return AccountConverter.toTransactionResponseDTOList(
				transactionMapper.getAccountTransaction(accountId));
		}

		return AccountConverter.toTransactionResponseDTOList(
			transactionMapper.filterAccountTransactions(accountId, transactionType));
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
					((Map<String, Object>) responseMap.get("data")).get("resDepositTrust");

			return accountList.stream()
					.map(accountMap -> AccountResponseDTO.from(AccountVO.from(accountMap, userId), userId))
					.toList();

		} catch (IOException e) {
			throw new ServerErrorException(GET_ACCOUNTS_LIST_FAILED);
		}


	}

    public void saveAccounts(final Long userId) {
        try {
            userService.validateUserExists(userId);

            String accountListJson = codefAccountService.getAccountList();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(accountListJson, Map.class);

            List<Map<String, Object>> accountList = (List<Map<String, Object>>)
                    ((Map<String, Object>) responseMap.get("data")).get("resDepositTrust");

            for (Map<String, Object> account : accountList) {
                String accountId = (String) account.get("resAccount");
                if (accountMapper.existsByAccountId(accountId)) {
                    log.info("중복 계좌 건너뜀: {}", accountId);
                    continue;
                }

                // 잔액 데이터 String -> Long 타입으로 형 변환
                String balanceStr = (String) account.get("resAccountBalance");
                Long balance = 0L;
                if (balanceStr != null && !balanceStr.isEmpty()) {
                    balance = Long.parseLong(balanceStr);
                }

                AccountVO vo = AccountVO.builder()
                        .userId(userId)
                        .accountId((String) account.get("resAccount"))
                        .accountName((String) account.get("resAccountName"))
                        .accountType(AccountType.valueOf("person"))
                        .ownerId(userId)
                        .balance(balance)
                        .accountCurrency((String) account.get("resAccountCurrency"))
                        .isDeleted(DeletedStatus.N)
                        .build();

                accountMapper.saveAccount(vo);
            }

        } catch (Exception e) {
            throw new ServerErrorException(SAVE_ACCOUNTS_LIST_FAILED);
        }
    }
}
