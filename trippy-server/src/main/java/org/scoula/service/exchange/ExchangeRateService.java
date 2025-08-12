package org.scoula.service.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.scoula.controller.exchange.dto.response.AccountListDTO;
import org.scoula.controller.exchange.dto.response.ExchangeBalanceDTO;
import org.scoula.domain.exchange.AccountListVO;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.domain.exchange.ExchangeRequest;
import org.scoula.mapper.exchange.ExchangeRateMapper;
import org.scoula.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeRateService {

	private final ExchangeRateMapper exchangeRateMapper;
	private final UserService userService;

	/***
	 * 환율 리스트 조회
	 * @return
	 */
	public List<ExchangeRateVO> getExchangeRates() {
		return exchangeRateMapper.getExchangeRateList();
	}

	/***
	 * 계좌 리스트 조회
	 * @param userId
	 * @return
	 */
	public List<AccountListDTO> getAccountList(Long userId) {
		userService.validateUserExists(userId);

		List<AccountListVO> accountList = exchangeRateMapper.getAccountList(userId);

		return Optional.ofNullable(accountList)
			.orElse(Collections.emptyList())
			.stream()
			.map(AccountListDTO::from)
			.toList();
	}

	/***
	 * 오늘의 환율, 해당 계좌의 KRW 잔액, 지정 외화의 잔액을 한 번에 반환.
	 * @param userId
	 * @param currencyCode
	 * @param accountId
	 * @return
	 */
	public ExchangeBalanceDTO getRatesAndBalance(Long userId, String currencyCode, String accountId) {
		ExchangeRateVO exchangeRateVO = exchangeRateMapper.findTodayRateByCurrencyCode(currencyCode);
		Double rate = exchangeRateVO.getBaseExchangeRate();

		AccountListVO accountListVo = exchangeRateMapper.findKrwBalanceByAccountId(accountId);
		Long krwBalance = accountListVo.getBalance();

		Double foreignBalance = exchangeRateMapper.findForeignBalanceByAccountIdAndCurrency(userId, currencyCode);

		return ExchangeBalanceDTO.from(currencyCode, rate, krwBalance, foreignBalance);
	}

	@Transactional
	public void exchange(Long userId, ExchangeRequest exchangeRequest) {
		Long krwAmount = exchangeRequest.krwAmount();
		String krwAccountId = exchangeRequest.krwAccountId();
		double foreignAmount = exchangeRequest.foreignAmount();
		String foreignAccountId = exchangeRequest.foreignAccountId();
		String currencyCode = exchangeRequest.currencyCode();

		exchangeRateMapper.insertNewTransactionKrw(krwAmount, krwAccountId, userId);
		exchangeRateMapper.updateForeignAmount(foreignAmount, foreignAccountId, currencyCode);
		exchangeRateMapper.updateKrwAmount(krwAmount, krwAccountId, userId);
	}
}