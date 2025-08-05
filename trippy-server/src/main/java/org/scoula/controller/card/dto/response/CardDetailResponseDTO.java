package org.scoula.controller.card.dto.response;

import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "카드 설정 화면에 표시할 상세 카드 정보 DTO")
public class CardDetailResponseDTO {

	@ApiModelProperty(value = "카드 ID", example = "1")
	private Long cardId;

	@ApiModelProperty(value = "유저 ID", example = "1001")
	private Long userId;

	@ApiModelProperty(value = "계좌 ID", example = "acc-001")
	private String accountId;

	@ApiModelProperty(value = "카드 번호", example = "1234-****-****-5678")
	private String cardNumber;

	@ApiModelProperty(value = "카드 이름", example = "국민카드")
	private String cardName;

	@ApiModelProperty(value = "카드 닉네임", example = "내 메인카드")
	private String cardNickname;

	@ApiModelProperty(value = "카드 식별번호 (field)", example = "7")
	private Integer field;

	@ApiModelProperty(value = "대표 카드 여부", example = "true")
	private Boolean isMainCard;
}
