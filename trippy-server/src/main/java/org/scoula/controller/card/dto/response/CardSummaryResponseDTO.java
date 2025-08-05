package org.scoula.controller.card.dto.response;

import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "결제 화면에 표시할 카드 요약 정보 DTO")
public class CardSummaryResponseDTO {

	@ApiModelProperty(value = "카드 ID", example = "1")
	private Long cardId;

	@ApiModelProperty(value = "유저 ID", example = "1001")
	private Long userId;

	@ApiModelProperty(value = "계좌 ID", example = "acc-001")
	private String accountId;

	@ApiModelProperty(value = "카드 이름", example = "국민카드")
	private String cardName;

	@ApiModelProperty(value = "카드 이미지 URL", example = "https://example.com/card.png")
	private String cardImg;

	@ApiModelProperty(value = "대표 카드 여부", example = "true")
	private Boolean isMainCard;

	@ApiModelProperty(value = "카드 닉네임", example = "내 메인카드")
	private String cardNickname;
}
