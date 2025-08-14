package org.scoula.controller.transfer.dto.request;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "특정 나라 조회 DTO")
public record ExchangeRequestDTO(
	@ApiModelProperty(value = "국가 코드 목록", example = "[\"USD\", \"EUR\", \"JPY(100)\"]", dataType = "List")
	List<String> currencyCode
) {
}
