package org.scoula.service.exchange;

public interface ExchangeRateService {

    /* 환율 API로 환율 데이터 가져오는 함수 */
//    List<ExchangeRateApiDTO> fetchAndSaveExchangeRates();
    void fetchAndSaveExchangeRates();

//    String getExchangeRate( String currencyCode, LocalDateTime exchangeRateDate);

}
