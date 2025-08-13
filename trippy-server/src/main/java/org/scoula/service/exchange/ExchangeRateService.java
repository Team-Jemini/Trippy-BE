package org.scoula.service.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.scoula.controller.exchange.dto.ExchangeRateDTO;
import org.scoula.controller.exchange.dto.response.AccountListDTO;
import org.scoula.controller.exchange.dto.response.ExchangeBalanceDTO;
import org.scoula.controller.exchange.dto.response.ExchangeChangeRateDTO;
import org.scoula.domain.exchange.AccountListVO;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.domain.exchange.ExchangeRequest;
import org.scoula.mapper.exchange.ExchangeRateMapper;
import org.scoula.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeRateService {

	private final ExchangeRateMapper exchangeRateMapper;
	private final UserService userService;

	/***
	 * 환율 리스트 조회
	 * 1. 최근 2일간의 데이터를 전체 조회
	 * 2. 통화별로 그룹핑 후 오늘/어제 데이터 분리
	 * 3. 변화량, 변화율, 상승/하락 여부 계산
	 * @return
	 */
	public List<ExchangeChangeRateDTO> getExchangeRates() {
		List<ExchangeRateVO> recentTwoDaysExchangeRateList = exchangeRateMapper.getRecentTwoDaysExchangeRates();

		Map<String, List<ExchangeRateVO>> groupedByCurrency = recentTwoDaysExchangeRateList.stream()
			.filter(vo -> !"KRW".equals(vo.getCurrencyCode()))
			.collect(Collectors.groupingBy(
				ExchangeRateVO::getCurrencyCode,
				LinkedHashMap::new,
				Collectors.toList()
			));

		List<ExchangeChangeRateDTO> result = new ArrayList<>();

		for (Map.Entry<String, List<ExchangeRateVO>> entry : groupedByCurrency.entrySet()) {
			List<ExchangeRateVO> currencyData = entry.getValue();

			// 날짜순 정렬 (최신 날짜가 먼저 오도록)
			currencyData.sort((a, b) -> b.getExchangeRateDate().compareTo(a.getExchangeRateDate()));

			ExchangeRateVO todayData = currencyData.get(0);
			ExchangeRateVO yesterdayData = currencyData.get(1);
			result.add(calculateExchangeRateComparison(todayData, yesterdayData));
		}
		return result;
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

	/***
	 * 오늘/어제 환율 비교
	 * 1. JPY(100), IDR(100)은 100단위 기준이므로 실제 환율로 변환
	 * 2. 변화한 금액값과 변화량 차이, 그리고 +.- 여부 계산
	 * 3. 변화한 금액이 - 값이면 절대값 처리해서 리턴
	 * @param todayData
	 * @param yesterdayData
	 * @return
	 */
	private ExchangeChangeRateDTO calculateExchangeRateComparison(ExchangeRateVO todayData,
		ExchangeRateVO yesterdayData) {

		Double todayRate = todayData.getBaseExchangeRate();
		Double yesterdayRate = yesterdayData.getBaseExchangeRate();

		// 모든 통화 동일하게 처리 (DB 값 그대로)
		Double changeAmount = todayRate - yesterdayRate;
		Double changePercentage = (changeAmount / yesterdayRate) * 100;
		String upOrDown = changeAmount >= 0 ? "+" : "-";

		// 소수점 2자리까지 반올림
		Double roundedChangeAmount = Math.round(Math.abs(changeAmount) * 100.0) / 100.0;
		Double roundedChangePercentage = Math.round(Math.abs(changePercentage) * 100.0) / 100.0;

		return new ExchangeChangeRateDTO(
			todayData.getCurrencyName(),
			todayData.getBaseExchangeRate(),
			upOrDown,
			roundedChangeAmount,
			roundedChangePercentage,
			todayData.getCurrencyCode()
		);
	}
}