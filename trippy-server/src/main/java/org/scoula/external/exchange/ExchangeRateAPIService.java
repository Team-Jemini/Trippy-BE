package org.scoula.external.exchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.external.exchange.dto.ExchangeRateApiDTO;
import org.scoula.mapper.exchange.ExchangeRateApiMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExchangeRateAPIService {

	private final ExchangeRateApiMapper exchangeRateApiMapper;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final String BASE_URL = "https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=";

	@Value("${exchange.api.key}")
	private String API_KEY;

	/***
	 * 토요일, 일요일은 금요일 환율로 설정
	 */
	public LocalDateTime getExchangeRateDate() {
		LocalDateTime exchangeRateDate = LocalDateTime.now();
		int dayOfWeek = exchangeRateDate.getDayOfWeek().getValue();
		if (dayOfWeek == 6) {
			exchangeRateDate = exchangeRateDate.minusDays(1);
		} else if (dayOfWeek == 7) {
			exchangeRateDate = exchangeRateDate.minusDays(2);
		}
		return exchangeRateDate;
	}

	/***
	 * 날짜 형식 yyyyMMdd 로 변환
	 */
	public String getFormattedDate(LocalDateTime exchangeRateDate) {
		return exchangeRateDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}

	/***
	 * 환율 API로 환율 데이터 가져오는 함수
	 */
	@Scheduled(cron = " 0 5 11 * * * ") // 오전 11시 5분 스케쥴러 실행
	@Transactional
	public void fetchAndSaveExchangeRates() {
		LocalDateTime exchangeRateDate = getExchangeRateDate();
		String formattedDate = getFormattedDate(exchangeRateDate);

		//환율 API
		String url = BASE_URL + API_KEY + "&searchdate=" + formattedDate + "&data=AP01";

		try {
			String json = restTemplate.getForObject(url, String.class);
			List<ExchangeRateApiDTO> apiDtoList = objectMapper.readValue(json,
				new TypeReference<List<ExchangeRateApiDTO>>() {
				});

			//API DTO -> 환율 VO
			for (ExchangeRateApiDTO dto : apiDtoList) {
				ExchangeRateVO exchangeRateVO = dto.toExchangeRateVO(dto.baseExchangeRate());
				exchangeRateApiMapper.save(exchangeRateVO);
			}
		} catch (Exception e) {
			log.error(ErrorCode.EXCHANGE_RATE_NOT_FOUND_EXCEPTION + " : {}", e.getMessage());
		}
	}
}
