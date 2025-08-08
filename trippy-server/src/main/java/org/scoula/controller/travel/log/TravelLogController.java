package org.scoula.controller.travel.log;

import java.util.List;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.travel.log.dto.req.TravelLogCreateDTO;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.service.travel.TravelLogService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "Travel")
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "[JWT] 여행 로그 생성", notes = "여행 로그를 새로 생성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행 로그 생성 성공", response = SuccessNonDataResponse.class),
            @ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
    })
    public SuccessNonDataResponse createTravelLog(
            @RequestParam Long userId,
            @RequestPart("travelLog") TravelLogCreateDTO travelLogCreateDTO,
            @RequestPart(value = "travelImg", required = false) MultipartFile travelImg
    ) {
        travelLogService.createTravelLog(userId, travelLogCreateDTO, travelImg);
        return SuccessNonDataResponse.success(SuccessCode.CREATE_TRAVEL_LOG_SUCCESS);
    }


}
