package org.scoula.controller.voucher;

import java.util.List;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
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

@Api(tags = "Voucher", description = "항공권, 숙소, 관광바우처 예약 내역을 관리합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/air-ticket")
public class AirTicketController {
	private final AirTicketService airTicketService;

	/**
	 * 추후에 @ApiIgnore @RequestParam Long userId와 같은 형식으로 수정해야 합니다.
	 * 그리고 헤더에 담긴 JWT 토큰으로 유저 정보를 받아와서 알아서 userId 파라미터로 넣어줍니다...
	 * 8/5(화)에 JWT 작업 예정
	 * **/
	@ApiOperation(value = "[JWT] 항공권 전제 조회", notes = "항공권 전체 조회 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "항공권 전체조회 성공입니다.", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public SuccessResponse<List<AirTicketDto>> getAirTicket(@RequestParam Long userId) {

		return SuccessResponse.success(SuccessCode.FIND_AIR_TICKET_SUCCESS, airTicketService.getAirTicket(userId));
	}

	@ApiOperation(value = "[JWT] 항공권 상세 조회", notes = "항공권 상세 조회 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "항공권 상세조회 성공입니다.", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 항공권Id가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/{airLineId}")
	public SuccessResponse<AirTicketDetailDto> getAirTicket(@RequestParam Long userId,
		@ApiParam(value = "항공권 Id", required = true, example = "1") @PathVariable Long airLineId) {

		return SuccessResponse.success(SuccessCode.FIND_DETAIL_AIR_TICKET_SUCCESS,
			airTicketService.getAirTicketDetail(userId, airLineId));
	}

}
