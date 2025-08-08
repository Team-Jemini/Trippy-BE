package org.scoula.controller.travel.log;

import java.util.List;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.service.travel.TravelLogService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

@Api(tags = "Travel Log")
@RestController
@RequiredArgsConstructor
@RequestMapping("/travel-log")
public class TravelLogController {

	private final TravelLogService travelLogService;

	@ApiOperation(value = "[JWT] 여행 로그 전체 조회", notes = "여행 로그 전체 조회 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "여행 로그 전체조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping
	public SuccessResponse<List<TravelLogDTO>> getTravelLogs(
		@RequestParam Long userId
	) {
		return SuccessResponse.success(SuccessCode.FIND_TRAVEL_LOG_SUCCESS, travelLogService.getTravelLogs(userId));
	}
}
