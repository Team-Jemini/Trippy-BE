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

    private final ExchangeRateService service;

    @PostMapping("/post")
    public ResponseEntity<Void> test1() {
        service.fetchAndSaveExchangeRates();
        return ResponseEntity.ok().build();
    }
}
