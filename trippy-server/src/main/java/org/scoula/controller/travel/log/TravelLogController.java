package org.scoula.controller.travel.log;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.processing.SupportedAnnotationTypes;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.travel.log.dto.req.TravelLogCreateDTO;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.service.travel.TravelLogService;

import org.springframework.format.annotation.DateTimeFormat;
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
		@RequestHeader("userId") Long userId,

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


	@ApiOperation(
			value = "[JWT] 여행 날짜 중복 여부 확인",
			notes = "해당 유저의 기존 여행 로그 기간과 입력한 기간이 겹치는지 확인합니다. (겹치면 false, 가능하면 true)"
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "여행 날짜 가용성 확인 성공")
	})
	@GetMapping("/availability")
	public SuccessResponse<Boolean> checkAvailability(
			@ApiParam(value = "유저 ID", required = true, example = "101")
			@RequestHeader("X-USER-ID") Long userId,

			@ApiParam(value = "여행 시작일시 (ISO-8601)", required = true, example = "2025-07-10T09:00:00")
			@RequestParam("begin")
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
			LocalDateTime begin,

			@ApiParam(value = "여행 종료일시 (ISO-8601)", required = true, example = "2025-07-13T18:00:00")
			@RequestParam("end")
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
			LocalDateTime end
	) {
		boolean available = travelLogService.isTravelDateAvailable(userId, begin, end);
		return SuccessResponse.success(
				SuccessCode.CHECK_TRAVEL_DATE_AVAILABLE_SUCCESS,  // enum에 추가해 사용하세요
				available
		);
	}

}
