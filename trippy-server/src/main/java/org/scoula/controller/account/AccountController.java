package org.scoula.controller.account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.account.dto.response.AccountDTO;
import org.scoula.service.account.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Personal Account")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @ApiOperation(value = "내 계좌 목록 조회", notes = "내 계좌 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "내 계좌 목록 조회 성공", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 내부 오류입니다.", response = ErrorResponse.class)
    })
    @GetMapping()
    public SuccessResponse<List<AccountDTO>> getAccountsList(@RequestParam Long userId) {
        return SuccessResponse.success(SuccessCode.FIND_ACCOUNTS_LIST_SUCCESS, accountService.getAccountsList(userId));
    }
}
