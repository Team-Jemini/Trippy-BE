package org.scoula.controller.travel.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.travel.report.dto.req.TravelReportRequestDTO;
import org.scoula.controller.travel.report.dto.res.TravelReportDTO;
import org.scoula.service.travel.TravelReportService;
import org.springframework.web.bind.annotation.*;


import io.swagger.annotations.*;

@Slf4j
@Api(tags = "Travel")
@RestController
@RequestMapping("/travel-report")
@RequiredArgsConstructor
public class TravelReportController {
    private final TravelReportService travelReportService;

    @ApiOperation(value = "[JWT] 여행 리포트 조회", notes = "여행 ID로 소비 리포트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행 소비 리포트 조회 성공"),
            @ApiResponse(code = 404, message = "해당 여행이 존재하지 않습니다.")
    })
    @GetMapping("/{travelId}")
    public SuccessResponse<TravelReportDTO> getTravelReport(
            @ApiParam(value = "여행 ID", required = true, example = "5")
            @PathVariable Long travelId
    ) {
        return SuccessResponse.success(SuccessCode.FIND_TRAVEL_REPORT_SUCCESS, travelReportService.getTravelReport(travelId));
    }



    @ApiOperation(value = "[JWT] 여행 리포트 생성", notes = "travel_log의 account_id와 기간을 사용해 집계하고 저장합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "여행 소비 리포트 저장 성공")
    })
    @PostMapping
    public SuccessResponse<String> createTravelReport(@RequestBody TravelReportRequestDTO request) {
        travelReportService.createTravelReport(request);
        return SuccessResponse.success(SuccessCode.CREATE_TRAVEL_REPORT_SUCCESS, "travel_report 저장 완료");
    }
}
