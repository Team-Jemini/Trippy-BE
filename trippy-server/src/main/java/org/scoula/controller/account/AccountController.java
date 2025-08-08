package org.scoula.controller.account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.account.dto.response.AccountResponseDTO;
import org.scoula.controller.account.dto.response.PersonalAccountDetailResponseDTO;
import org.scoula.controller.groupAccount.dto.response.AccountTransactionResponseDTO;
import org.scoula.service.account.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Personal Account")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

	@ApiOperation(value = "내 보유 계좌 조회", notes = "내 보유 계좌 조회 - Codef")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "내 보유 계좌 조회 성공", response = SuccessResponse.class),
			@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "서버 내부 오류입니다.", response = ErrorResponse.class)
	})
	@GetMapping("/sync")
	public SuccessResponse<List<AccountResponseDTO>> getCodefAccount(@RequestParam Long userId) {
		return SuccessResponse.success(SuccessCode.GET_CODEF_DATA_SUCCESS, accountService.getCodefAccounts(userId));
	}

    @ApiOperation(value = "내 계좌 목록 조회", notes = "내 계좌 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "내 계좌 목록 조회 성공", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 내부 오류입니다.", response = ErrorResponse.class)
    })
    @GetMapping()
    public SuccessResponse<List<AccountResponseDTO>> getAccountsList(@RequestParam Long userId) {
        return SuccessResponse.success(SuccessCode.FIND_ACCOUNTS_LIST_SUCCESS, accountService.getAccountsList(userId));
    }
	@ApiOperation(value = "[JWT]개인 계좌 상세 조회", notes = "계좌 상세 조회.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "계좌 상세 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
	})
	@GetMapping("/detail")
	public SuccessResponse<PersonalAccountDetailResponseDTO> getGroupAccountDetail(
		@ApiParam(value = "개인 계좌 ID", required = true) @RequestParam String accountId,
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId
	) {
		return SuccessResponse.success(SuccessCode.GET_PERSONAL_ACCOUNT_DETAIL_SUCCESS,
			accountService.getPersonalAccountDetail(accountId, userId));
	}

	@ApiOperation(value = "[JWT]개인 계좌 상세 조회 with 필터", notes = "계좌 거래 내역 조회.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "계좌 거래 내역 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
	})
	@GetMapping("/transactions")
	public SuccessResponse<List<AccountTransactionResponseDTO>> filterAccountTransactions(
		@ApiParam(value = "개인 계좌 ID", required = true) @RequestParam String accountId,
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId,
		@ApiParam(value = "거래 타입", required = true) @RequestParam String transactionType
	) {
		return SuccessResponse.success(SuccessCode.FILTER_ACCOUNT_TRANSACTION_SUCCESS,
			accountService.filterAccountTransactions(accountId, userId, transactionType));
	}
}

