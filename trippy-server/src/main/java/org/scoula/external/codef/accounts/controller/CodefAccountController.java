package org.scoula.external.codef.accounts.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.domain.account.AccountVO;
import org.scoula.external.codef.accounts.service.CodefAccountService;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Codef Account")
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class CodefAccountController {
    private final CodefAccountService codefAccountService;

    @ApiOperation(value = "개인 계좌 조회", notes = "Codef 개인 계좌 조회 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Codef 계좌가 성공적으로 조회되었습니다.", response = SuccessResponse.class),
            @ApiResponse(code = 400, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
    })
    @PostMapping("/sync")
    public SuccessResponse<AccountVO> saveCodefAccount(@RequestParam Long userId) {
        codefAccountService.saveAccountsToDB(userId);
        return SuccessResponse.success(SuccessCode.GET_CODEF_DATA_SUCCESS, null);
    }
}
