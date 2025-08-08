package org.scoula.controller.exchange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.exchange.dto.response.AccountListDTO;
import org.scoula.controller.exchange.dto.response.ExchangeBalanceDTO;
import org.scoula.domain.exchange.ExchangeRateVO;
import org.scoula.external.exchange.ExchangeRateAPIService;
import org.scoula.service.exchange.ExchangeRateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Exchange")
@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange-rate")
public class ExchangeController {

    private final ExchangeRateAPIService ExchangeRateAPIService;
    private final ExchangeRateService ExchangeRateservice;

    @ApiOperation(value = "[JWT]환율 환전 관련", notes = "환율 관련 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "환율 정보 저장 성공", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "환율 정보가 존재하지 않습니다.", response = ErrorResponse.class),
    })
    @PostMapping()
    public SuccessNonDataResponse fetchAndSave() {
        ExchangeRateAPIService.fetchAndSaveExchangeRates();
        return SuccessNonDataResponse.success(SuccessCode.SAVE_EXCHANGE_RATE_SUCCESS);
    }

    @GetMapping("/getRateList")
    public SuccessResponse<List<ExchangeRateVO>> getExchangeRates() {
        return SuccessResponse.success(SuccessCode.FIND_EXCHANGE_RATE_SUCCESS, ExchangeRateservice.getExchangeRates());
        // JSON 형태로 출력
    }


    @GetMapping("/getAccountList")
    public SuccessResponse<List<AccountListDTO>> getAccountList(@RequestParam Long userId) {
        return SuccessResponse.success(SuccessCode.FIND_EXCHANGE_RATE_SUCCESS, ExchangeRateservice.getAccountList(userId));
    }

    @GetMapping("/getRateBal")
    public SuccessResponse<ExchangeBalanceDTO> getRatesAndBalance(@RequestParam String currencyCode, @RequestParam String userId, @RequestParam String accountId) {
        return SuccessResponse.success(SuccessCode.FIND_EXCHANGE_BALANCE_SUCCESS, ExchangeRateservice.getRatesAndBalance(currencyCode, accountId, userId));
    }

}
