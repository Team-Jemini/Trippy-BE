package org.scoula.service.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.scoula.controller.exchange.dto.response.AccountListDTO;
import org.scoula.controller.exchange.dto.response.ExchangeBalanceDTO;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.exchange.AccountListVO;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.domain.exchange.ForeignAccountBalanceVO;
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

	public List<AccountListDTO> getAccountList(Long userId) {
		List<AccountListVO> accListVO = exchangeRateMapper.getAccountList(userId);
		List<AccountListDTO> accListDto = new ArrayList<>();
		for(AccountListVO vo : accListVO) {
			accListDto.add(new AccountListDTO(vo.getAccountId(), vo.getAccountName(), vo.getBalance(), vo.getAccountCurrency(), vo.getIsDeleted()));
		}
		return accListDto;
	}

	public ExchangeBalanceDTO getRatesAndBalance(String currencyCode, String accountId, String userId) {
		ExchangeRateVO  exchangeRateVO = exchangeRateMapper.findTodayRateByCurrencyCode(currencyCode);
		Double rate = exchangeRateVO.getBaseExchangeRate();

		AccountListVO accountListVo = exchangeRateMapper.findKrwBalanceByAccountId(accountId);
		Long krwBalance = accountListVo.getBalance();

		Double foreignBalance = exchangeRateMapper.findForeignBalanceByAccountIdAndCurrency(userId, currencyCode);
		ExchangeBalanceDTO exchangeBalanceDTO = ExchangeBalanceDTO.from(currencyCode, rate, krwBalance, foreignBalance);

		return exchangeBalanceDTO;
	}
}