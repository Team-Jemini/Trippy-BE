package org.scoula.controller.card.dto.response;

import org.scoula.domain.card.CardVO;
import lombok.*;

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

	// ✅ CardVO로부터 변환하는 생성자
	public CardDetailResponseDTO(CardVO vo) {
		this.cardId = vo.getCardId();
		this.userId = vo.getUserId();
		this.accountId = vo.getAccountId();
		this.cardNumber = vo.getCardNumber();
		this.cardName = vo.getCardName();
		this.cardNickname = vo.getCardNickname();
		this.field = vo.getField();
		this.isMainCard = vo.getIsMainCard();
	}
}