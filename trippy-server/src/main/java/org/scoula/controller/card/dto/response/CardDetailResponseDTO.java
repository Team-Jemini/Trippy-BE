package org.scoula.controller.card.dto.response;

import lombok.*;
import io.swagger.annotations.ApiModelProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailResponseDTO {

	private Long cardId;

	private Long userId;

	private String accountId;

	private String cardNumber;

	private String cardName;

	private String cardNickname;

	private Integer field;

	private Boolean isMainCard;
}
