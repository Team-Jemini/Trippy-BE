package org.scoula.controller.voucher;

import java.util.List;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.config.resolver.UserId;
import org.scoula.controller.voucher.dto.response.AirTicketDetailDto;
import org.scoula.controller.voucher.dto.response.AirTicketDto;
import org.scoula.service.voucher.AirTicketService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Voucher", description = "항공권, 숙소, 관광바우처 예약 내역을 관리합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/air-ticket")
public class AirTicketController {
	private final AirTicketService airTicketService;

	@ApiOperation(value = "[JWT] 항공권 전체 조회", notes = "항공권 전체 조회 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "항공권 전체조회 성공입니다.", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping
	public SuccessResponse<List<AirTicketDto>> getAirTicket(@ApiIgnore @UserId Long userId) {

		return SuccessResponse.success(SuccessCode.FIND_AIR_TICKET_SUCCESS, airTicketService.getAirTicket(userId));
	}

	@ApiOperation(value = "[JWT] 항공권 상세 조회", notes = "항공권 상세 조회 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "항공권 상세조회 성공입니다.", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 항공권Id가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/{airLineId}")
	public SuccessResponse<AirTicketDetailDto> getAirTicket(@ApiIgnore @UserId Long userId,
		@ApiParam(value = "항공권 Id", required = true, example = "1") @PathVariable Long airLineId) {

		return SuccessResponse.success(SuccessCode.FIND_DETAIL_AIR_TICKET_SUCCESS,
			airTicketService.getAirTicketDetail(userId, airLineId));
	}

}
