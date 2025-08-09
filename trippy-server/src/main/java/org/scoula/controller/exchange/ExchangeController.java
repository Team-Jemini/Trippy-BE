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

	private final ExchangeRateAPIService exchangeRateAPIService;
	private final ExchangeRateService exchangeRateService;

	@ApiOperation(value = "[JWT] 수출입은행 API의 환율 정보 FETCH", notes = " 수출입은행 API의 환율 정보 FETCH API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "환율 정보 저장 성공", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "환율 정보가 존재하지 않습니다.", response = ErrorResponse.class),
	})
	@PostMapping()
	public SuccessNonDataResponse fetchAndSave() {
		exchangeRateAPIService.fetchAndSaveExchangeRates();
		return SuccessNonDataResponse.success(SuccessCode.SAVE_EXCHANGE_RATE_SUCCESS);
	}

	@ApiOperation(value = "[JWT] 저장된 최신 환율 목록 조회", notes = "저장된 최신 환율 목록을 반환하는 API")
	@ApiResponses({
		@ApiResponse(code = 200, message = "환율 정보 찾기 성공", response = SuccessResponse.class, responseContainer = "List"),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "환율 정보가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/rates")
	public SuccessResponse<List<ExchangeRateVO>> getExchangeRates() {
		return SuccessResponse.success(SuccessCode.FIND_EXCHANGE_RATE_SUCCESS, exchangeRateService.getExchangeRates());
	}


	@ApiOperation(value = "[JWT] 사용자의 계좌 목록 조회", notes = "사용자의 계좌 목록을 환전 뷰에 맞는 DTO로 반환 API")
	@ApiResponses({
		@ApiResponse(code = 200, message = "계좌 목록 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/accounts")
	public SuccessResponse<List<AccountListDTO>> getAccountList(@RequestParam Long userId) {
		return SuccessResponse.success(SuccessCode.FIND_ACCOUNTS_LIST_SUCCESS,
			exchangeRateService.getAccountList(userId));
	}


	@ApiOperation(value = "[JWT] 사용자의 오늘환율과 외화잔액 조회", notes = "사용자의 오늘환율과 외화잔액 조회 API")
	@ApiResponses({
			@ApiResponse(code = 200, message = "환율 잔액 찾기 성공", response = SuccessResponse.class),
			@ApiResponse(code = 400, message = "요청 파라미터 오류", response = ErrorResponse.class),
			@ApiResponse(code = 401, message = "인증 실패", response = ErrorResponse.class),
			@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/rate-balance")
	public SuccessResponse<ExchangeBalanceDTO> getRatesAndBalance(@RequestParam Long userId,
																  @RequestParam String currencyCode, @RequestParam String accountId) {
		return SuccessResponse.success(SuccessCode.FIND_EXCHANGE_BALANCE_SUCCESS,
				exchangeRateService.getRatesAndBalance(userId, currencyCode, accountId));
	}



    @PostMapping("/exchange")
    public SuccessNonDataResponse exchange(@RequestParam Long krwAmount,
                                           @RequestParam String krwAccountId,
                                           @RequestParam Long userId,
                                           @RequestParam String currencyCode,
                                           @RequestParam String foreignAccountId,
                                           @RequestParam double foreignAmount) {

        exchangeRateService.exchange(krwAmount, krwAccountId, userId, foreignAmount, foreignAccountId, currencyCode);

        return SuccessNonDataResponse.success(SuccessCode.EXCHANGE_SUCCESS);
    }


}
