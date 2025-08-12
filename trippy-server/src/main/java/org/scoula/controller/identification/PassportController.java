package org.scoula.controller.identification;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.identification.dto.PassportDTO;
import org.scoula.controller.identification.dto.req.PassportReq;
import org.scoula.service.identification.PassportService;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Passport", description = "여권 조회 및 등록하기 기능을 관리합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/passport")
public class PassportController {

	private final PassportService passportService;

	@ApiOperation(value = "[JWT] 여권 조회", notes = "여권을 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "여권 조회에 성공했습니다.", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
	})
	@GetMapping()
	public SuccessResponse<PassportDTO> getPassport(
		@RequestParam Long userId
	) {
		return SuccessResponse.success(SuccessCode.PASSPORT_GET_SUCCESS, passportService.getPassport(userId));
	}

	@ApiOperation(value = "[JWT] 여권 등록", notes = "여권을 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "여권 조회에 성공했습니다.", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
	})
	@PostMapping()
	public SuccessResponse<Integer> addPassport(
		@RequestParam Long userId,
		@ApiParam(value = "여권 정보", required = true) @RequestBody PassportReq passportReq
	) {
		return SuccessResponse.success(SuccessCode.PASSPORT_ADD_SUCCESS,
			passportService.addPassport(userId, passportReq));
	}

}
