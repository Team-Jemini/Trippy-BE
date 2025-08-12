package org.scoula.controller.travel.report.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "여행 리포트 요청")
public record TravelReportRequestDTO(
        @ApiModelProperty(value = "Travel Id", example = "1")
        Long travelId
) {}