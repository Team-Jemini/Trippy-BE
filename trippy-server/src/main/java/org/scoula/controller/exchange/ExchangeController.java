package org.scoula.controller.exchange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.exchange.dto.ExchRateAneBalanceResponse;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.service.exchange.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Exchange")
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange-rate")
public class ExchangeController {

    private final ExchangeRateService service;

    /* 환율 정보 저장하는 요청 처리 */
    @ApiOperation(value = "[JWT]환율 환전 관련", notes = "환율 관련 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "환율 정보 저장 성공", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "환율 정보가 존재하지 않습니다.", response = ErrorResponse.class),
    })
    @PostMapping()
    public SuccessNonDataResponse SaveExchangeRates() {
        service.fetchAndSaveExchangeRates();
        return SuccessNonDataResponse.success(SuccessCode.SAVE_EXCHANGE_RATE_SUCCESS);
    }


    /**
     * DB에서 환율 데이터 가져오는 함수
     */
    @GetMapping("/getList")
    public ResponseEntity<List<ExchangeRateVO>> getExchangeRates() {
        List<ExchangeRateVO> exchangeRates = service.getExchangeRates();
        return ResponseEntity.ok(exchangeRates);
        // JSON 형태로 출력
        // LocalDateTime 값 출력 시 오류 발생.
    }

    /* 환전 기능 */
    @GetMapping("/getBalance")
    public ResponseEntity<ExchRateAneBalanceResponse> getRatesAndBalance(@RequestParam String currencyCode, @RequestParam Long accountId) {
        ExchRateAneBalanceResponse response = service.getRatesAndBalance(currencyCode, accountId);
        return ResponseEntity.ok(response);
    }

}
