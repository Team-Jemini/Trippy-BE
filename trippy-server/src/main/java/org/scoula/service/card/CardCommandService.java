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
		int deleted = cardMapper.deleteCardById(cardId);
		if (deleted == 0) {
			throw new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND); // 카드용 에러코드 있으면 추가해도 됨
		}
	}
}
