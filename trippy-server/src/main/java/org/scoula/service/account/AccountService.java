package org.scoula.service.account;

import java.util.List;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.TrippyException;
import org.scoula.controller.account.dto.response.PersonalAccountDetailResponseDTO;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.domain.transaction.TransactionVO;
import org.scoula.mapper.account.AccountMapper;
import org.scoula.mapper.transaction.TransactionMapper;
import org.scoula.service.groupaccount.AccountConverter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountService {

	final AccountMapper accountMapper;
	final TransactionMapper transactionMapper;

	public PersonalAccountDetailResponseDTO getPersonalAccountDetail(String accountId, Long userId) {

		log.info("getPersonalAccountDetail");
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
}
