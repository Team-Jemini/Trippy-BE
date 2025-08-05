package org.scoula.controller.travel.log;

import java.util.List;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.domain.travel.TravelLogVO;
import org.scoula.service.travel.TravelLogService;

import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

@Api(tags = "Travel Log")
@RestController
@RequiredArgsConstructor
@RequestMapping("/travel-log")
public class TravelLogController {

    private final TravelLogService travelLogService;

    /**
     * 추후 JWT 기반 유저 인증이 붙는다면, userId는 @ApiIgnore로 대체하고 헤더에서 추출할 수 있음
     */
    @ApiOperation(value = "[JWT] 여행 로그 전체 조회", notes = "여행 로그 전체 조회 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행 로그 전체조회 성공", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "해당 유저의 여행 로그가 없습니다.", response = ErrorResponse.class)
    })
    @GetMapping
    public SuccessResponse<List<TravelLogVO>> getTravelLogs(
            @ApiParam(value = "유저 ID", required = true, example = "1")
            @RequestParam Long userId
    ) {
        List<TravelLogVO> logs = travelLogService.getAllTravelLogs(); // 실사용 시 userId 필터링도 추가 가능
        return SuccessResponse.success(SuccessCode.FIND_TRAVEL_LOG_SUCCESS, logs);
    }
}
