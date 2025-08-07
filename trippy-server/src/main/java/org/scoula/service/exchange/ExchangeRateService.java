package org.scoula.service.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.scoula.controller.exchange.dto.response.AccountListDTO;
import org.scoula.controller.exchange.dto.response.ExchangeBalanceDTO;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.mapper.account.AccountMapper;
import org.scoula.mapper.exchange.ExchangeRateMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.web.savedrequest.FastHttpDateFormat.getCurrentDate;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

	private final ExchangeRateMapper exchangeRateMapper;
//	private final AccountMapper accountMapper;

	/***
	 * DB에서 환율 데이터 조회
	 */
	public List<ExchangeRateVO> getExchangeRates() {
		return exchangeRateMapper.getExchangeRateList();
	}

	/***
	 * 환전
	 */

	public List<AccountListDTO> getAccountList(String userId) {
		List<AccountListDTO> accListDto = exchangeRateMapper.getAccountList(userId);



		log.info("userID : {}",userId);
		log.info("accListDto : {}",accListDto);
		return accListDto;
	}

	public ExchangeBalanceDTO getRatesAndBalance(String currencyCode, String accountId) {
		Double rate = exchangeRateMapper.findTodayRateByCurrencyCode(currencyCode, LocalDateTime.parse(getCurrentDate()));
		Long krwBalance = exchangeRateMapper.findKrwBalanceByAccountId(accountId);
		Double foreignBalance = exchangeRateMapper.findForeignBalanceByAccountIdAndCurrency(accountId, currencyCode);

		return ExchangeBalanceDTO.from(currencyCode, rate, krwBalance, foreignBalance);
	}
}