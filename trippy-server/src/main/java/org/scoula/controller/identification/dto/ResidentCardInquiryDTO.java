package org.scoula.controller.identification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "신분증 DTO")
public record ResidentCardInquiryDTO(
        @ApiModelProperty(value = "사용자 이름")
        String resUserName,
        @ApiModelProperty(value = "발행 날짜")
        String resIssueDate,
        @ApiModelProperty(value = "주민 등록 번호")
        String resUserIdentity,
        @ApiModelProperty(value = "거주지 주소")
        String address
) {}
