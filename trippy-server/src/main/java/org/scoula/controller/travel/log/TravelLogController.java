package org.scoula.controller.travel.log;

import java.util.List;

import javax.annotation.processing.SupportedAnnotationTypes;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.travel.log.dto.req.TravelLogCreateDTO;
import org.scoula.controller.travel.log.dto.req.TravelLogTransactionListDTO;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.service.travel.TravelLogService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@Api(tags = "Travel", description = "여행 소비 리포트를 관리합니다.")
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
		@ApiParam(value = "유저 ID", required = true, example = "101")
		@RequestParam Long userId
	) {
		return SuccessResponse.success(SuccessCode.FIND_TRAVEL_LOG_SUCCESS, travelLogService.getTravelLogs(userId));
	}

	@ApiOperation(value = "[JWT] 여행 로그 생성", notes = "여행 로그를 새로 생성하는 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "여행 로그 생성 성공", response = SuccessNonDataResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public SuccessNonDataResponse createTravelLog(
		@ApiParam(value = "유저 ID", required = true, example = "101")
		@RequestParam Long userId,

		@ApiParam(value = "여행 이미지 파일")
		@RequestPart(value = "travelImg", required = false)
		@NotNull @NotBlank
		MultipartFile travelImg,

		@ApiParam(value = "여행 로그 JSON (예: {\"title\":\"제주도 가족 여행\", \"travelBeginDate\":\"2025-09-01T12:00:00\", \"travelEndDate\":\"2025-09-04T12:00:00\", \"destination\":\"제주도\", \"isGenerated\":false})", required = true)
		@RequestPart("travelLogCreateDTO") TravelLogCreateDTO travelLogCreateDTO
	) {
		travelLogService.createTravelLog(userId, travelLogCreateDTO, travelImg);
		return SuccessNonDataResponse.success(SuccessCode.CREATE_TRAVEL_LOG_SUCCESS);
	}

	@ApiOperation(value = "[JWT] [지도뷰 보기] 여행 로그에서의 여행 기간 동안의 결제 내역 전체 조회", notes = "지도에 핀으로 보여질 결제 내역들 리스트 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "여행 기간 동안의 결제 내역 전체 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/{travelId}")
	public SuccessResponse<TravelLogTransactionListDTO> getTravelLogs(
		@ApiParam(value = "유저 ID", required = true, example = "1")
		@RequestParam Long userId,
		@ApiParam(value = "여행 ID", required = true, example = "1")
		@PathVariable Long travelId) {
		return SuccessResponse.success(SuccessCode.FIND_TRAVEL_LOG_TRANSACTIONS_SUCCESS, travelLogService.getTravelTransactions(userId, travelId));
	}

}
