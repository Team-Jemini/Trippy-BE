package org.scoula.controller.card.dto.response;

import org.scoula.domain.card.CardVO;

import lombok.*;

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

	// ✅ CardVO로부터 변환하는 생성자
	public CardSummaryResponseDTO(CardVO vo) {
		this.cardId = vo.getCardId();
		this.userId = vo.getUserId();
		this.accountId = vo.getAccountId();
		this.cardName = vo.getCardName();
		this.cardImg = vo.getCardImg();
		this.isMainCard = vo.getIsMainCard();
		this.cardNickname = vo.getCardNickname();
	}
}