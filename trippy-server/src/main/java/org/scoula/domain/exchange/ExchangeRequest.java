package org.scoula.domain.exchange;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "환전 요청 DTO")
public record ExchangeRequest(
	@ApiModelProperty(value = "환전 요청 금액", example = "500")
	Long krwAmount,
	@ApiModelProperty(value = "계좌 Id(한국)", example = "3333-02-123456")
	String krwAccountId,
	@ApiModelProperty(value = "환전할 통화 코드", example = "JPY")
	String currencyCode,
	@ApiModelProperty(value = "계좌 Id(외화)", example = "3333-02-123461")
	String foreignAccountId,
	@ApiModelProperty(value = "환전 요청 금액(외화)", example = "500")
	double foreignAmount
) {
}
