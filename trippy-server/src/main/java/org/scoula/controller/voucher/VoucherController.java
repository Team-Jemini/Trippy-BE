package org.scoula.controller.voucher;

import java.util.List;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.voucher.dto.response.AirTicketDto;
import org.scoula.controller.voucher.dto.response.VoucherDto;
import org.scoula.service.voucher.VoucherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Voucher")
@RestController
@RequiredArgsConstructor
@RequestMapping("/voucher")
public class VoucherController {

	private final VoucherService voucherService;

	@ApiOperation(value = "[JWT] 바우처(숙소,관광) 전제 조회", notes = "바우처(숙소,관광) 전체 조회 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "바우처(숙소,관광) 전체조회 성공입니다.", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping()
	public SuccessResponse<VoucherDto> getAirTicket(@RequestParam Long userId) {
		return SuccessResponse.success(SuccessCode.FIND_VOUCHER_SUCCESS, voucherService.getVouchers(userId));
	}

}
