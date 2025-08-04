package org.scoula.service.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.controller.exchange.dto.ExchangeRateApiDTO;
import org.scoula.controller.exchange.dto.ExchangeRateDTO;
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
public class ExchangeRateServiceImpl implements ExchangeRateService {

    /* 생성자 주입 (3개) */
    private final ExchangeRateApiMapper apiMapper;
    /* 환율 API 호출을 위한 RestTemplate */
    private final RestTemplate restTemplate = new RestTemplate();
    /* Json 데이터를 Java 객체로 변환을 위한 ObjectMapper */
    private final ObjectMapper objectMapper = new ObjectMapper();


    /* 환율 API 연결을 위한 인증키 */
    @Value("${exchange.api.key}")
    private String API_KEY;


    /* 환율 API로 환율 데이터 가져오는 함수 */
    @Override
    @Scheduled(cron = " 0 5 11 * * * ") // 11시 5분 스케쥴러 코드 실행
    @Transactional
    public void fetchAndSaveExchangeRates() {

        // 호출 시 날짜 정보 (yyyyMMdd 형식)
        LocalDateTime exchangeRateDate = LocalDateTime.now();

        // 토요일, 일요일은 금요일 환율로 설정
        int dayOfWeek = exchangeRateDate.getDayOfWeek().getValue();
        if (dayOfWeek == 6) {
            exchangeRateDate = exchangeRateDate.minusDays(1);
        } else if (dayOfWeek == 7) {
            exchangeRateDate = exchangeRateDate.minusDays(2);
        }
        String formattedDate = exchangeRateDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 환율 API
        String url = "https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + API_KEY + "&searchdate="+ formattedDate +"&data=AP01";

        try {
            String json = restTemplate.getForObject(url, String.class);
            List<ExchangeRateApiDTO> apiDtoList = objectMapper.readValue(json, new TypeReference<List<ExchangeRateApiDTO>>() {});

            // API DTO를 환율 DTO로 변환
            for (ExchangeRateApiDTO dto : apiDtoList) {

                ExchangeRateDTO entity = new ExchangeRateDTO();

                // 통화코드, 국가명 및 통화이름
                entity.setCurrencyCode(dto.getCurrencyCode());
                entity.setCurrencyName(dto.getCurrencyName());

                // 매매 기준율 환율
                String raw = dto.getBaseExchangeRate();
                String withoutComma = raw.replace(",", "");
                Double doubleBaseExchangeRate = Double.parseDouble(withoutComma);
                entity.setBaseExchangeRate(doubleBaseExchangeRate);

                // 환율날짜
                entity.setExchangeRateDate(exchangeRateDate);

                // 살 떄 환율, 팔 때 환율
                // -> 매매 기준율에서 가공해서 사용?
                entity.setRateBuy(doubleBaseExchangeRate);
                entity.setRateSell(doubleBaseExchangeRate);

                // 생성 날짜, 수정 날짜는 환율 날짜와 동일하게 유지.
                entity.setCreatedAt(LocalDateTime.now());
                entity.setUpdatedAt(LocalDateTime.now());

                // DB에 저장
                apiMapper.insertDataToVO(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
