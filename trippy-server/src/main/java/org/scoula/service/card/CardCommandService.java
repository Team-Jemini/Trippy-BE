package org.scoula.service.card;


import lombok.RequiredArgsConstructor;
import org.scoula.mapper.card.CardMapper;
import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardCommandService {

	private final CardMapper cardMapper;

	public void deleteCard(Long cardId) {
		int deleted = cardMapper.deleteById(cardId);
		if (deleted == 0) {
			throw new NotFoundException(ErrorCode.CARD_NOT_FOUND);
		}
	}

	public void createCardNickname(Long cardId, String cardNickname) {
		int updated = cardMapper.updateCardNickname(cardId, cardNickname);
		if (updated == 0) {
			throw new NotFoundException(ErrorCode.CARD_NOT_FOUND);
		}
	}

	public void updateCardNickname(Long cardId, String cardNickname) {
		int updated = cardMapper.updateCardNickname(cardId, cardNickname);
		if (updated == 0) {
			throw new NotFoundException(ErrorCode.CARD_NOT_FOUND);
		}
	}


	public void setMainCard(Long cardId) {
		Long userId = cardMapper.findUserIdByCardId(cardId);
		if (userId == null) {
			throw new NotFoundException(ErrorCode.CARD_NOT_FOUND);
		}

		cardMapper.unsetMainCardByUserId(userId);
		cardMapper.setMainCard(cardId);
	}
}
