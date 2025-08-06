package org.scoula.controller.card.dto.response;

import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardSummaryResponseDTO {

	private Long cardId;

	private Long userId;

	private String accountId;

	private String cardName;

	private String cardImg;

	private Boolean isMainCard;

	private String cardNickname;
}
