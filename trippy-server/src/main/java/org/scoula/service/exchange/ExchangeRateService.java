package org.scoula.service.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.scoula.controller.exchange.dto.response.AccountListDTO;
import org.scoula.controller.exchange.dto.response.ExchangeBalanceDTO;
import org.scoula.domain.exchange.AccountListVO;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.mapper.exchange.ExchangeRateMapper;
import org.scoula.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

		var list = Optional.ofNullable(exchangeRateMapper.getAccountList(userId))
			.orElseGet(List::of);

		return list.stream()
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
		userService.validateUserExists(userId);
		
		ExchangeRateVO exchangeRateVO = exchangeRateMapper.findTodayRateByCurrencyCode(currencyCode);
		Double rate = exchangeRateVO.getBaseExchangeRate();

		AccountListVO accountListVo = exchangeRateMapper.findKrwBalanceByAccountId(accountId);
		Long krwBalance = accountListVo.getBalance();

		Double foreignBalance = exchangeRateMapper.findForeignBalanceByAccountIdAndCurrency(userId, currencyCode);

		return ExchangeBalanceDTO.from(currencyCode, rate, krwBalance, foreignBalance);
	}

	public void exchange(Long krwAmount,
						 String krwAccountId,
						 Long userId,
						 double foreignAmount,
						 String foreignAccountId,
						 String currencyCode) {
		/**
		 * 거래내역 추가
		 */
		exchangeRateMapper.insertNewTransactionKrw(krwAmount, krwAccountId, userId);

		/**
		 * 외화 잔액 수정
		 */
		exchangeRateMapper.updateForeignAmount(foreignAmount, foreignAccountId, currencyCode);

		/**
		 * 원화 잔액 수정
		 */
		exchangeRateMapper.updateKrwAmount(krwAmount, krwAccountId, userId);
	}
}