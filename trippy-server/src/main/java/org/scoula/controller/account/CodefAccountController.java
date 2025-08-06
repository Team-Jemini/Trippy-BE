package org.scoula.controller.account;

import lombok.RequiredArgsConstructor;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.service.account.AccountService;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;

@Api(tags = "Codef Account")
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class CodefAccountController {
    private final AccountService accountService;

    @ApiOperation(value = "개인 계좌 조회", notes = "Codef 개인 계좌 조회 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Codef 계좌가 성공적으로 조회되었습니다.", response = SuccessResponse.class),
            @ApiResponse(code = 400, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
    })
    @PostMapping(value = "/sync")
    public SuccessNonDataResponse saveCodefAccount(@RequestParam Long userId) {
        accountService.saveAccounts(userId);
        return SuccessNonDataResponse.success(SuccessCode.GET_CODEF_DATA_SUCCESS);
    }
}
