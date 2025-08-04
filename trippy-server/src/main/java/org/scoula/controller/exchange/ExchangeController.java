package org.scoula.controller.exchange;

import lombok.RequiredArgsConstructor;
import org.scoula.service.exchange.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange-rate")
public class ExchangeController {

    // 생성자 주입
    private final ExchangeRateService service;

    /* 환율 정보 저장하는 요청 처리 */
    @PostMapping("/post")
    public ResponseEntity<Void> SaveExchangeRates() {
        service.fetchAndSaveExchangeRates();
        return ResponseEntity.ok().build();
    }
}
