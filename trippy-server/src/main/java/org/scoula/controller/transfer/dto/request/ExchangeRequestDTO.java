package org.scoula.controller.transfer.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "특정 나라 조회 DTO")
public record ExchangeRequestDTO (
    @ApiModelProperty(value = "국가 코드", example = "USD")
    String currencyCode
) {}
